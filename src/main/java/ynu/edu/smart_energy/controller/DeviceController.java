package ynu.edu.smart_energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ynu.edu.smart_energy.common.ApiResponse;
import ynu.edu.smart_energy.dto.MeterDeviceDTO;
import ynu.edu.smart_energy.dto.req.DeviceCreateReq;
import ynu.edu.smart_energy.dto.req.DeviceUpdateReq;
import ynu.edu.smart_energy.service.IDeviceService;

import java.util.List;

@Tag(name = "设备管理")
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final IDeviceService deviceService;

    @PostMapping
    public ApiResponse<MeterDeviceDTO> add(@RequestBody DeviceCreateReq req) {
        return ApiResponse.ok(deviceService.addDevice(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<MeterDeviceDTO> update(@PathVariable Long id, @RequestBody DeviceUpdateReq req) {
        return ApiResponse.ok(deviceService.updateDevice(id, req));
    }

    @PutMapping("/{id}/remove")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        deviceService.removeDevice(id);
        return ApiResponse.ok("拆除成功", null);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<MeterDeviceDTO> updateStatus(@PathVariable Long id,
                                                    @RequestParam String status) {
        return ApiResponse.ok(deviceService.updateDeviceStatus(id, status));
    }

    @GetMapping("/{id}")
    public ApiResponse<MeterDeviceDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(deviceService.getDeviceById(id));
    }

    @GetMapping
    public ApiResponse<List<MeterDeviceDTO>> listAll() {
        return ApiResponse.ok(deviceService.getAllDevices());
    }

    @GetMapping("/active")
    public ApiResponse<List<MeterDeviceDTO>> listActive() {
        return ApiResponse.ok(deviceService.getActiveDevices());
    }
}
