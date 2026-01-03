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
        name = "energy_record",
        indexes = {
                @Index(name = "idx_record_device_time", columnList = "device_id,collected_at")
        }
)
public class EnergyRecord {

    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 外键：所属设备 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private MeterDevice device;

    /** 当前电压(V) */
    @Column(nullable = false)
    private Double voltage;

    /** 当前电流(A) */
    @Column(nullable = false)
    private Double current;

    /** 当前实时功率(W) = voltage * current（需在业务层保证一致性） */
    @Column(nullable = false)
    private Double power;

    /** 累计用电量(kWh) */
    @Column(name = "total_kwh", nullable = false)
    private Double totalKwh;

    /** 采集时间戳 */
    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;
}
