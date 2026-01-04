package ynu.edu.smart_energy.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ynu.edu.smart_energy.entity.EnergyRecord;

import java.util.List;
import java.util.Optional;

public interface IEnergyRecordDao extends JpaRepository<EnergyRecord, Long> {

    /** 查询某设备最近10条采集数据（用于展示最新数据/折线图） */
    List<EnergyRecord> findTop10ByDevice_IdOrderByCollectedAtDesc(Long deviceId);

    /** 查询某设备最近N条采集数据（推荐使用 Pageable 控制条数） */
    @Query("SELECT e FROM EnergyRecord e WHERE e.device.id = ?1 ORDER BY e.collectedAt DESC")
    List<EnergyRecord> findRecentByDeviceId(Long deviceId, Pageable pageable);

    Optional<EnergyRecord> findTopByDeviceIdOrderByCollectedAtDesc(Long deviceId);
}
