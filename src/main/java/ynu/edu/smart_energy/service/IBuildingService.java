package ynu.edu.smart_energy.service;

import ynu.edu.smart_energy.dto.BuildingDTO;
import ynu.edu.smart_energy.entity.Building;

import java.util.List;

public interface IBuildingService {

    BuildingDTO addBuilding(Building building);

    BuildingDTO updateBuilding(Long id, Building building);

    void deleteBuilding(Long id);

    BuildingDTO getBuildingById(Long id);

    List<BuildingDTO> getAllBuildings();
}
