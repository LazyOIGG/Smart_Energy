package ynu.edu.smart_energy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ynu.edu.smart_energy.common.ApiResponse;
import ynu.edu.smart_energy.dto.AlarmRecordDTO;
import ynu.edu.smart_energy.service.IAlarmQueryService;

import java.util.List;

@Tag(name = "告警管理")
@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final IAlarmQueryService alarmQueryService;

    @Operation(summary = "查询所有告警（倒序）")
    @GetMapping
    public ApiResponse<List<AlarmRecordDTO>> listAll() {
        return ApiResponse.ok(alarmQueryService.getAllAlarms());
    }

    @Operation(summary = "按设备ID查询告警（倒序）")
    @GetMapping("/byDevice")
    public ApiResponse<List<AlarmRecordDTO>> listByDevice(@RequestParam Long deviceId) {
        return ApiResponse.ok(alarmQueryService.getAlarmsByDeviceId(deviceId));
    }

    @Operation(summary = "按设备SN查询告警（倒序）")
    @GetMapping("/bySn")
    public ApiResponse<List<AlarmRecordDTO>> listBySn(@RequestParam String sn) {
        return ApiResponse.ok(alarmQueryService.getAlarmsBySn(sn));
    }
}
