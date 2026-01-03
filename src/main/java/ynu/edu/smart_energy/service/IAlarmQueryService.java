package ynu.edu.smart_energy.service;

import ynu.edu.smart_energy.dto.AlarmRecordDTO;

import java.util.List;

public interface IAlarmQueryService {

    List<AlarmRecordDTO> getAllAlarms();

    List<AlarmRecordDTO> getAlarmsByDeviceId(Long deviceId);

    List<AlarmRecordDTO> getAlarmsBySn(String sn);
}
