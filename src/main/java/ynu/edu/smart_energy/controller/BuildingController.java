package ynu.edu.smart_energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ynu.edu.smart_energy.common.ApiResponse;
import ynu.edu.smart_energy.dto.BuildingDTO;
import ynu.edu.smart_energy.entity.Building;
import ynu.edu.smart_energy.service.IBuildingService;

import java.util.List;

@Tag(name = "建筑管理")
@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final IBuildingService buildingService;

    @PostMapping
    public ApiResponse<BuildingDTO> add(@RequestBody Building building) {
        return ApiResponse.ok(buildingService.addBuilding(building));
    }

    @PutMapping("/{id}")
    public ApiResponse<BuildingDTO> update(@PathVariable Long id, @RequestBody Building building) {
        return ApiResponse.ok(buildingService.updateBuilding(id, building));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ApiResponse.ok("删除成功", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<BuildingDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(buildingService.getBuildingById(id));
    }

    @GetMapping
    public ApiResponse<List<BuildingDTO>> list() {
        return ApiResponse.ok(buildingService.getAllBuildings());
    }
}
