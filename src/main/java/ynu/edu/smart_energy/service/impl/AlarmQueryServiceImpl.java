package ynu.edu.smart_energy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ynu.edu.smart_energy.common.BizException;
import ynu.edu.smart_energy.dao.IAlarmRecordDao;
import ynu.edu.smart_energy.dao.IMeterDeviceDao;
import ynu.edu.smart_energy.dto.AlarmRecordDTO;
import ynu.edu.smart_energy.entity.AlarmRecord;
import ynu.edu.smart_energy.entity.MeterDevice;
import ynu.edu.smart_energy.mapper.DtoMapper;
import ynu.edu.smart_energy.service.IAlarmQueryService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmQueryServiceImpl implements IAlarmQueryService {

    private final IAlarmRecordDao alarmRecordDao;
    private final IMeterDeviceDao meterDeviceDao;
    private final DtoMapper dtoMapper;

    @Override
    public List<AlarmRecordDTO> getAllAlarms() {

        List<AlarmRecord> list = alarmRecordDao.findAll();

        // 按触发时间倒序（findAll默认无序）
        list.sort(Comparator.comparing(AlarmRecord::getTriggeredAt).reversed());

        return list.stream()
                .map(dtoMapper::toAlarmDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlarmRecordDTO> getAlarmsByDeviceId(Long deviceId) {

        // 校验设备存在
        meterDeviceDao.findById(deviceId)
                .orElseThrow(() -> new BizException("设备不存在，id=" + deviceId));

        List<AlarmRecord> list = alarmRecordDao.findAllByDevice_IdOrderByTriggeredAtDesc(deviceId);

        return list.stream()
                .map(dtoMapper::toAlarmDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlarmRecordDTO> getAlarmsBySn(String sn) {

        if (sn == null || sn.trim().isEmpty()) {
            throw new BizException("设备SN不能为空");
        }

        MeterDevice device = meterDeviceDao.findBySn(sn)
                .orElseThrow(() -> new BizException("设备SN不存在：" + sn));

        List<AlarmRecord> list = alarmRecordDao.findAllByDevice_IdOrderByTriggeredAtDesc(device.getId());

        return list.stream()
                .map(dtoMapper::toAlarmDTO)
                .collect(Collectors.toList());
    }
}
