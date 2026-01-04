package ynu.edu.smart_energy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ynu.edu.smart_energy.common.ApiResponse;
import ynu.edu.smart_energy.dto.EnergyRecordDTO;
import ynu.edu.smart_energy.service.IEnergyService;

import java.util.List;

@Tag(name = "能耗数据查询")
@RestController
@RequestMapping("/api/energy")
@RequiredArgsConstructor
public class EnergyController {

    private final IEnergyService energyService;

    @Operation(summary = "查询某设备最新一条能耗数据")
    @GetMapping("/latest")
    public ApiResponse<EnergyRecordDTO> latest(@RequestParam Long deviceId) {
        return ApiResponse.ok(energyService.getLatestRecord(deviceId));
    }

    @Operation(summary = "查询某设备最近N条能耗数据（用于折线图）")
    @GetMapping("/recent")
    public ApiResponse<List<EnergyRecordDTO>> recent(@RequestParam Long deviceId,
                                                     @RequestParam(defaultValue = "10") Integer limit) {
        return ApiResponse.ok(energyService.getRecentRecords(deviceId, limit));
    }
}
