package ynu.edu.smart_energy.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 能耗记录DTO
 */
@Data
public class EnergyRecordDTO {

    private Long id;

    private Long deviceId;
    private String deviceSn;

    private Double voltage;
    private Double current;
    private Double power;
    private Double totalKwh;

    private LocalDateTime collectedAt;
}
