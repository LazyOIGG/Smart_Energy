package ynu.edu.smart_energy.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 建筑DTO：只返回建筑信息，不返回 devices（避免循环引用）
 */
@Data
public class BuildingDTO {

    private Long id;
    private String name;
    private String locationCode;
    private Integer floors;
    private String usageType;
    private LocalDateTime createdAt;
}
