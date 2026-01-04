package ynu.edu.smart_energy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ynu.edu.smart_energy.common.ApiResponse;
import ynu.edu.smart_energy.dto.stats.DevicePowerPointDTO;
import ynu.edu.smart_energy.service.IStatsService;

import java.util.List;

@Tag(name = "统计分析")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final IStatsService statsService;

    @Operation(summary = "折线图：某设备最近10次功率趋势")
    @GetMapping("/device/power10")
    public ApiResponse<List<DevicePowerPointDTO>> power10(@RequestParam Long deviceId) {
        return ApiResponse.ok(statsService.getRecent10PowerTrend(deviceId));
    }
}
