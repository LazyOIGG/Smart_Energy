package ynu.edu.smart_energy.service;

import ynu.edu.smart_energy.dto.EnergyRecordDTO;

import java.util.List;

public interface IEnergyService {

    EnergyRecordDTO getLatestRecord(Long deviceId);

    List<EnergyRecordDTO> getRecentRecords(Long deviceId, Integer limit);
}
