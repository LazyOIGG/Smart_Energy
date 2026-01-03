package ynu.edu.smart_energy.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ynu.edu.smart_energy.dto.*;
import ynu.edu.smart_energy.entity.*;

@Component
@RequiredArgsConstructor
public class DtoMapper {

    private final ModelMapper modelMapper;

    public BuildingDTO toBuildingDTO(Building b) {
        if (b == null) return null;
        return modelMapper.map(b, BuildingDTO.class);
    }

    public MeterDeviceDTO toDeviceDTO(MeterDevice d) {
        if (d == null) return null;
        MeterDeviceDTO dto = modelMapper.map(d, MeterDeviceDTO.class);
        if (d.getBuilding() != null) {
            dto.setBuildingId(d.getBuilding().getId());
            dto.setBuildingName(d.getBuilding().getName());
        }
        return dto;
    }

    public EnergyRecordDTO toEnergyDTO(EnergyRecord r) {
        if (r == null) return null;
        EnergyRecordDTO dto = modelMapper.map(r, EnergyRecordDTO.class);
        if (r.getDevice() != null) {
            dto.setDeviceId(r.getDevice().getId());
            dto.setDeviceSn(r.getDevice().getSn());
        }
        return dto;
    }

    public AlarmRecordDTO toAlarmDTO(AlarmRecord a) {
        if (a == null) return null;
        AlarmRecordDTO dto = modelMapper.map(a, AlarmRecordDTO.class);
        if (a.getDevice() != null) {
            dto.setDeviceId(a.getDevice().getId());
            dto.setDeviceSn(a.getDevice().getSn());
        }
        return dto;
    }
}
