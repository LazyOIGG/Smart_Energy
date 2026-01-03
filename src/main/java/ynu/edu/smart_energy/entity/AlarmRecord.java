package ynu.edu.smart_energy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "alarm_record",
        indexes = {
                @Index(name = "idx_alarm_time", columnList = "triggered_at"),
                @Index(name = "idx_alarm_device_time", columnList = "device_id,triggered_at")
        }
)
public class AlarmRecord {

    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 外键：所属设备 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private MeterDevice device;

    /** 告警类型：OVERLOAD功率超限 / VOLTAGE_ABNORMAL电压异常 */
    @Column(name = "alarm_type", nullable = false, length = 32)
    private String alarmType;

    /** 告警数值（功率W 或 电压V） */
    @Column(name = "alarm_value", nullable = false)
    private Double alarmValue;

    /** 告警详情描述 */
    @Column(length = 255)
    private String detail;

    /** 触发时间 */
    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt;
}
