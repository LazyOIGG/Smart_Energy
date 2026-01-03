package ynu.edu.smart_energy.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 告警记录DTO
 */
@Data
public class AlarmRecordDTO {

    private Long id;

    private Long deviceId;
    private String deviceSn;

    private String alarmType;
    private Double alarmValue;
    private String detail;

    private LocalDateTime triggeredAt;
}
