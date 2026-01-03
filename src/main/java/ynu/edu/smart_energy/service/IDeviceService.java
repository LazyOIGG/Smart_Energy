package ynu.edu.smart_energy.service;

import ynu.edu.smart_energy.dto.MeterDeviceDTO;
import ynu.edu.smart_energy.dto.req.DeviceCreateReq;
import ynu.edu.smart_energy.dto.req.DeviceUpdateReq;

import java.util.List;

public interface IDeviceService {

    MeterDeviceDTO addDevice(DeviceCreateReq req);

    MeterDeviceDTO updateDevice(Long id, DeviceUpdateReq req);

    void removeDevice(Long id);

    MeterDeviceDTO updateDeviceStatus(Long id, String status);

    MeterDeviceDTO getDeviceById(Long id);

    List<MeterDeviceDTO> getAllDevices();

    List<MeterDeviceDTO> getActiveDevices();
}
