package ynu.edu.smart_energy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ynu.edu.smart_energy.common.BizException;
import ynu.edu.smart_energy.dao.IBuildingDao;
import ynu.edu.smart_energy.dao.IMeterDeviceDao;
import ynu.edu.smart_energy.dto.MeterDeviceDTO;
import ynu.edu.smart_energy.dto.req.DeviceCreateReq;
import ynu.edu.smart_energy.dto.req.DeviceUpdateReq;
import ynu.edu.smart_energy.entity.Building;
import ynu.edu.smart_energy.entity.MeterDevice;
import ynu.edu.smart_energy.mapper.DtoMapper;
import ynu.edu.smart_energy.service.IDeviceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceServiceImpl implements IDeviceService {

    private final IMeterDeviceDao meterDeviceDao;
    private final IBuildingDao buildingDao;
    private final DtoMapper dtoMapper;

    @Override
    public MeterDeviceDTO addDevice(DeviceCreateReq req) {
        if (req.getName() == null || req.getName().trim().isEmpty()) throw new BizException("设备名称不能为空");
        if (req.getSn() == null || req.getSn().trim().isEmpty()) throw new BizException("设备SN不能为空");
        if (req.getRoomNo() == null || req.getRoomNo().trim().isEmpty()) throw new BizException("房间号不能为空");
        if (req.getPmax() == null || req.getPmax() <= 0) throw new BizException("Pmax必须大于0");
        if (req.getBuildingId() == null) throw new BizException("buildingId不能为空");

        Building building = buildingDao.findById(req.getBuildingId())
                .orElseThrow(() -> new BizException("建筑不存在，id=" + req.getBuildingId()));

        if (meterDeviceDao.findBySn(req.getSn()).isPresent()) {
            throw new BizException("设备SN已存在：" + req.getSn());
        }

        boolean exists = meterDeviceDao.existsByBuilding_IdAndRoomNoAndActiveTrue(req.getBuildingId(), req.getRoomNo());
        if (exists) {
            throw new BizException("该建筑该房间已有有效电表，请先拆除旧设备或停用旧设备");
        }

        MeterDevice device = MeterDevice.builder()
                .name(req.getName())
                .sn(req.getSn())
                .status(req.getStatus() == null ? "ONLINE" : req.getStatus())
                .pmax(req.getPmax())
                .building(building)
                .roomNo(req.getRoomNo())
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        return dtoMapper.toDeviceDTO(meterDeviceDao.save(device));
    }

    @Override
    public MeterDeviceDTO updateDevice(Long id, DeviceUpdateReq req) {
        MeterDevice db = meterDeviceDao.findById(id)
                .orElseThrow(() -> new BizException("设备不存在，id=" + id));

        if (req.getName() != null && !req.getName().trim().isEmpty()) db.setName(req.getName());
        if (req.getPmax() != null && req.getPmax() > 0) db.setPmax(req.getPmax());

        if (req.getRoomNo() != null && !req.getRoomNo().trim().isEmpty()) {
            boolean exists = meterDeviceDao.existsByBuilding_IdAndRoomNoAndActiveTrue(db.getBuilding().getId(), req.getRoomNo());
            if (exists && !req.getRoomNo().equals(db.getRoomNo())) {
                throw new BizException("该房间已有有效电表，不能修改到该房间");
            }
            db.setRoomNo(req.getRoomNo());
        }

        return dtoMapper.toDeviceDTO(meterDeviceDao.save(db));
    }

    @Override
    public void removeDevice(Long id) {
        MeterDevice db = meterDeviceDao.findById(id)
                .orElseThrow(() -> new BizException("设备不存在，id=" + id));
        db.setActive(false);
        meterDeviceDao.save(db);
    }

    @Override
    public MeterDeviceDTO updateDeviceStatus(Long id, String status) {
        MeterDevice db = meterDeviceDao.findById(id)
                .orElseThrow(() -> new BizException("设备不存在，id=" + id));

        if (!"ONLINE".equals(status) && !"OFFLINE".equals(status)) {
            throw new BizException("状态只能是 ONLINE 或 OFFLINE");
        }
        db.setStatus(status);
        return dtoMapper.toDeviceDTO(meterDeviceDao.save(db));
    }

    @Override
    @Transactional(readOnly = true)
    public MeterDeviceDTO getDeviceById(Long id) {
        return dtoMapper.toDeviceDTO(meterDeviceDao.findById(id)
                .orElseThrow(() -> new BizException("设备不存在，id=" + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeterDeviceDTO> getAllDevices() {
        return meterDeviceDao.findAll()
                .stream()
                .map(dtoMapper::toDeviceDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeterDeviceDTO> getActiveDevices() {
        return meterDeviceDao.findAllByActiveTrue()
                .stream()
                .map(dtoMapper::toDeviceDTO)
                .collect(Collectors.toList());
    }
}
