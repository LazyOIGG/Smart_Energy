package ynu.edu.smart_energy.dto.req;

import lombok.Data;

/**
 * 更新设备请求体
 */
@Data
public class DeviceUpdateReq {

    private String name;
    private Double pmax;
    private String roomNo;
}
