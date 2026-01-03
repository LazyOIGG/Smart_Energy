package ynu.edu.smart_energy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ynu.edu.smart_energy.common.BizException;
import ynu.edu.smart_energy.dao.IEnergyRecordDao;
import ynu.edu.smart_energy.dao.IMeterDeviceDao;
import ynu.edu.smart_energy.dto.EnergyRecordDTO;
import ynu.edu.smart_energy.entity.EnergyRecord;
import ynu.edu.smart_energy.entity.MeterDevice;
import ynu.edu.smart_energy.mapper.DtoMapper;
import ynu.edu.smart_energy.service.IEnergyService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnergyServiceImpl implements IEnergyService {

    private final IEnergyRecordDao energyRecordDao;
    private final IMeterDeviceDao meterDeviceDao;
    private final DtoMapper dtoMapper;

    @Override
    public EnergyRecordDTO getLatestRecord(Long deviceId) {

        // 校验设备是否存在
        MeterDevice device = meterDeviceDao.findById(deviceId)
                .orElseThrow(() -> new BizException("设备不存在，id=" + deviceId));

        // 查最新1条（按 collectedAt DESC）
        List<EnergyRecord> list = energyRecordDao.findRecentByDeviceId(device.getId(), PageRequest.of(0, 1));
        if (list.isEmpty()) {
            return null;
        }

        return dtoMapper.toEnergyDTO(list.get(0));
    }

    @Override
    public List<EnergyRecordDTO> getRecentRecords(Long deviceId, Integer limit) {

        // 参数保护
        if (limit == null || limit <= 0) limit = 10;
        if (limit > 200) limit = 200;

        // 校验设备存在
        meterDeviceDao.findById(deviceId)
                .orElseThrow(() -> new BizException("设备不存在，id=" + deviceId));

        // 查询最近N条
        List<EnergyRecord> list = energyRecordDao.findRecentByDeviceId(deviceId, PageRequest.of(0, limit));

        // Entity -> DTO
        return list.stream()
                .map(dtoMapper::toEnergyDTO)
                .collect(Collectors.toList());
    }
}
