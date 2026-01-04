package ynu.edu.smart_energy.service.simulator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ynu.edu.smart_energy.dao.IEnergyRecordDao;
import ynu.edu.smart_energy.dao.IMeterDeviceDao;
import ynu.edu.smart_energy.entity.EnergyRecord;
import ynu.edu.smart_energy.entity.MeterDevice;
import ynu.edu.smart_energy.service.IAlarmService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

/**
 * 模拟器服务：定时为每台在线电表生成能耗数据
 * 规则：
 * 1）每5~10秒生成一次（本示例：10秒）
 * 2）电压在210~235波动（正态/随机波动）
 * 3）功率随时间变化：白天高、夜间低
 * 4）每20~50条正常后注入1条异常（过载或电压异常）
 * 5）保证 power = voltage * current
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SimulatorService {

    private final IMeterDeviceDao meterDeviceDao;
    private final IEnergyRecordDao energyRecordDao;
    private final IAlarmService alarmService;

    private final Random random = new Random();

    /*
     * 当前累计的正常数据条数，用于决定何时注入异常
     */

    private int normalCount = 0;

    /*
     * 下一次异常注入阈值（20~50）
     */

    private int nextFaultThreshold = 20 + random.nextInt(31);


    /*
     * 定时任务：每10秒运行一次（可以改成5000~10000）
     */

    @Scheduled(fixedRate = 10000)
    public void generateEnergyRecords() {

        // 只对 active=true 的设备生成
        List<MeterDevice> devices = meterDeviceDao.findAllByActiveTrue();
        if (devices.isEmpty()) {
            log.warn("当前没有有效设备，无法生成能耗数据");
            return;
        }

        for (MeterDevice device : devices) {

            // 设备必须 ONLINE 才生成
            if (!"ONLINE".equals(device.getStatus())) {
                continue;
            }

            EnergyRecord record = simulateOneRecord(device);
            energyRecordDao.save(record);

            // 告警检测
            alarmService.checkAndSaveAlarm(device, record);

            log.info("模拟数据已写入：设备SN={} V={} I={} P={} kWh={}",
                    device.getSn(),
                    record.getVoltage(),
                    record.getCurrent(),
                    record.getPower(),
                    record.getTotalKwh());
        }
    }

    /*
     * 模拟生成一条记录
     */


    private EnergyRecord simulateOneRecord(MeterDevice device) {

        boolean injectFault = shouldInjectFault();

        double voltage = 210 + random.nextDouble() * 25;

        double basePower = generateBasePower(device.getPmax(), LocalTime.now());

        if (injectFault) {
            int faultType = random.nextInt(2);
            if (faultType == 0) {
                basePower = device.getPmax() * (1.1 + random.nextDouble() * 0.2);
            } else {
                if (random.nextBoolean()) {
                    voltage = 180 + random.nextDouble() * 15;
                } else {
                    voltage = 245 + random.nextDouble() * 15;
                }
            }
        }

        double current = basePower / voltage;
        double power = voltage * current;

        LocalDateTime now = LocalDateTime.now();

        // 查询上一条记录，获取真实累计值与真实采样间隔
        EnergyRecord last = energyRecordDao.findTopByDeviceIdOrderByCollectedAtDesc(device.getId())
                .orElse(null);

        double lastTotal = (last == null) ? 0.0 : last.getTotalKwh();

        LocalDateTime lastTime = (last == null) ? now.minusSeconds(8) : last.getCollectedAt();
        long intervalSeconds = Math.max(1, java.time.Duration.between(lastTime, now).getSeconds());

        // 本次新增电量：kWh = W * 秒 / 3600000
        double deltaKwh = power * intervalSeconds / 3600000.0;

        double totalKwh = lastTotal + deltaKwh;

        return EnergyRecord.builder()
                .device(device)
                .voltage(round2(voltage))
                .current(round3(current))
                .power(round2(power))
                .totalKwh(round3(totalKwh))
                .collectedAt(now)
                .build();
    }


    /*
     * 生成基础功率：白天（8:00~22:00）高，夜间低
     */


    private double generateBasePower(double pmax, LocalTime now) {
        int hour = now.getHour();

        // 夜间：0~6点低功率 10%~30%
        if (hour >= 0 && hour <= 6) {
            return pmax * (0.1 + random.nextDouble() * 0.2);
        }

        // 白天：8~22点 40%~90%
        if (hour >= 8 && hour <= 22) {
            return pmax * (0.4 + random.nextDouble() * 0.5);
        }

        // 早晚：6~8 & 22~24 30%~50%
        return pmax * (0.3 + random.nextDouble() * 0.2);
    }

    /*
     * 判断是否应该注入异常：每20~50条正常后出现一次异常
     */


    private boolean shouldInjectFault() {
        normalCount++;

        if (normalCount >= nextFaultThreshold) {
            // 触发异常
            log.warn("本次将注入异常数据（阈值={}）", nextFaultThreshold);

            // 重置计数器，并生成下一个阈值
            normalCount = 0;
            nextFaultThreshold = 20 + random.nextInt(31);

            return true;
        }
        return false;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
