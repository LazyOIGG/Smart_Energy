package ynu.edu.smart_energy.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 折线图点：时间 + 功率
 */
@Data
@AllArgsConstructor
public class DevicePowerPointDTO {

    private LocalDateTime time;
    private Double power;
}
