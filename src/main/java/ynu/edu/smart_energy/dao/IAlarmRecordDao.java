package ynu.edu.smart_energy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ynu.edu.smart_energy.entity.AlarmRecord;

import java.util.List;

public interface IAlarmRecordDao extends JpaRepository<AlarmRecord, Long> {

    /** 查询某设备所有告警记录（按时间倒序） */
    List<AlarmRecord> findAllByDevice_IdOrderByTriggeredAtDesc(Long deviceId);
}
