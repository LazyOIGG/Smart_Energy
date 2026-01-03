package ynu.edu.smart_energy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ynu.edu.smart_energy.dao.IAlarmRecordDao;
import ynu.edu.smart_energy.entity.AlarmRecord;
import ynu.edu.smart_energy.entity.EnergyRecord;
import ynu.edu.smart_energy.entity.MeterDevice;
import ynu.edu.smart_energy.service.IAlarmService;

import java.time.LocalDateTime;

/**
 * 告警规则：
 * 1）功率超限：power > Pmax
 * 2）电压异常：电压偏离220V超过±10%，即 voltage < 198 或 voltage > 242
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AlarmServiceImpl implements IAlarmService {

    private final IAlarmRecordDao alarmRecordDao;

    @Override
    public void checkAndSaveAlarm(MeterDevice device, EnergyRecord record) {

        // 规则1：功率超限
        if (record.getPower() != null && device.getPmax() != null && record.getPower() > device.getPmax()) {
            AlarmRecord alarm = AlarmRecord.builder()
                    .device(device)
                    .alarmType("OVERLOAD")
                    .alarmValue(record.getPower())
                    .detail("功率超限：" + record.getPower() + "W > 阈值 " + device.getPmax() + "W")
                    .triggeredAt(LocalDateTime.now())
                    .build();
            alarmRecordDao.save(alarm);
        }

        // 规则2：电压异常（偏离220超过±10%）
        if (record.getVoltage() != null && (record.getVoltage() < 198 || record.getVoltage() > 242)) {
            AlarmRecord alarm = AlarmRecord.builder()
                    .device(device)
                    .alarmType("VOLTAGE_ABNORMAL")
                    .alarmValue(record.getVoltage())
                    .detail("电压异常：" + record.getVoltage() + "V（正常范围：198~242V）")
                    .triggeredAt(LocalDateTime.now())
                    .build();
            alarmRecordDao.save(alarm);
        }
    }
}
