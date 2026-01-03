package ynu.edu.smart_energy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ynu.edu.smart_energy.common.BizException;
import ynu.edu.smart_energy.dao.IBuildingDao;
import ynu.edu.smart_energy.dao.IMeterDeviceDao;
import ynu.edu.smart_energy.dto.BuildingDTO;
import ynu.edu.smart_energy.entity.Building;
import ynu.edu.smart_energy.mapper.DtoMapper;
import ynu.edu.smart_energy.service.IBuildingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingServiceImpl implements IBuildingService {

    private final IBuildingDao buildingDao;
    private final IMeterDeviceDao meterDeviceDao;
    private final DtoMapper dtoMapper;

    @Override
    public BuildingDTO addBuilding(Building building) {
        if (building.getName() == null || building.getName().trim().isEmpty()) {
            throw new BizException("建筑名称不能为空");
        }
        building.setId(null);
        if (building.getCreatedAt() == null) {
            building.setCreatedAt(LocalDateTime.now());
        }
        return dtoMapper.toBuildingDTO(buildingDao.save(building));
    }

    @Override
    public BuildingDTO updateBuilding(Long id, Building building) {
        Building db = buildingDao.findById(id)
                .orElseThrow(() -> new BizException("建筑不存在，id=" + id));

        if (building.getName() != null && !building.getName().trim().isEmpty()) db.setName(building.getName());
        if (building.getLocationCode() != null) db.setLocationCode(building.getLocationCode());
        if (building.getFloors() != null) db.setFloors(building.getFloors());
        if (building.getUsageType() != null) db.setUsageType(building.getUsageType());

        return dtoMapper.toBuildingDTO(buildingDao.save(db));
    }

    @Override
    public void deleteBuilding(Long id) {
        Building db = buildingDao.findById(id)
                .orElseThrow(() -> new BizException("建筑不存在，id=" + id));

        boolean hasDevice = meterDeviceDao.existsByBuilding_IdAndActiveTrue(id);
        if (hasDevice) {
            throw new BizException("该建筑下存在有效设备，不能删除");
        }
        buildingDao.delete(db);
    }

    @Override
    @Transactional(readOnly = true)
    public BuildingDTO getBuildingById(Long id) {
        return dtoMapper.toBuildingDTO(buildingDao.findById(id)
                .orElseThrow(() -> new BizException("建筑不存在，id=" + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuildingDTO> getAllBuildings() {
        return buildingDao.findAll()
                .stream()
                .map(dtoMapper::toBuildingDTO)
                .collect(Collectors.toList());
    }
}
