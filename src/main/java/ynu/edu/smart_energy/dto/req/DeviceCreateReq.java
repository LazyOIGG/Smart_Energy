package ynu.edu.smart_energy.dto.req;

import lombok.Data;

/**
 * 新增设备请求体
 */
@Data
public class DeviceCreateReq {

    private String name;
    private String sn;
    private String status;
    private Double pmax;
    private Long buildingId;
    private String roomNo;
}
