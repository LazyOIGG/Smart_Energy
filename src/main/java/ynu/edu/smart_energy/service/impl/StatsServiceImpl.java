package ynu.edu.smart_energy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ynu.edu.smart_energy.common.BizException;
import ynu.edu.smart_energy.dao.IEnergyRecordDao;
import ynu.edu.smart_energy.dao.IMeterDeviceDao;
import ynu.edu.smart_energy.dto.stats.DevicePowerPointDTO;
import ynu.edu.smart_energy.entity.EnergyRecord;
import ynu.edu.smart_energy.service.IStatsService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements IStatsService {

    private final IEnergyRecordDao energyRecordDao;
    private final IMeterDeviceDao meterDeviceDao;

    @Override
    public List<DevicePowerPointDTO> getRecent10PowerTrend(Long deviceId) {

        // 校验设备存在
        meterDeviceDao.findById(deviceId)
                .orElseThrow(() -> new BizException("设备不存在，id=" + deviceId));

        // 最近10条（倒序）
        List<EnergyRecord> records = energyRecordDao.findTop10ByDevice_IdOrderByCollectedAtDesc(deviceId);

        if (records.isEmpty()) {
            throw new BizException("该设备暂无采集数据");
        }

        // 折线图一般按时间正序返回更舒服
        return records.stream()
                .sorted(Comparator.comparing(EnergyRecord::getCollectedAt))
                .map(r -> new DevicePowerPointDTO(r.getCollectedAt(), r.getPower()))
                .collect(Collectors.toList());
    }
}
