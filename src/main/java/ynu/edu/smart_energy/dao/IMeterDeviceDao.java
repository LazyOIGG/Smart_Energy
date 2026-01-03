package ynu.edu.smart_energy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ynu.edu.smart_energy.entity.MeterDevice;

import java.util.List;
import java.util.Optional;

public interface IMeterDeviceDao extends JpaRepository<MeterDevice, Long> {

    /** 根据 SN 查询设备 */
    Optional<MeterDevice> findBySn(String sn);

    /** 查询所有有效设备 */
    List<MeterDevice> findAllByActiveTrue();

    /**
     * 判断同一建筑同一房间是否已有有效设备
     * 用于约束：同一建筑同一房间同一时间只能绑定一个有效电表
     */
    boolean existsByBuilding_IdAndRoomNoAndActiveTrue(Long buildingId, String roomNo);

    boolean existsByBuilding_IdAndActiveTrue(Long buildingId);
}
