package ynu.edu.smart_energy.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备DTO：只返回必要字段
 */
@Data
public class MeterDeviceDTO {

    private Long id;
    private String name;
    private String sn;
    private String status;
    private Double pmax;
    private String roomNo;
    private Boolean active;
    private LocalDateTime createdAt;

    // 建筑信息（只给必要字段，不嵌套 BuildingEntity）
    private Long buildingId;
    private String buildingName;
}
