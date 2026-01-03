package ynu.edu.smart_energy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "meter_device",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_device_sn", columnNames = {"sn"})
        },
        indexes = {
                @Index(name = "idx_building_room_active", columnList = "building_id,room_no,active")
        }
)
public class MeterDevice {

    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 设备名称 */
    @Column(nullable = false, length = 64)
    private String name;

    /** 唯一设备序列号 SN */
    @Column(nullable = false, length = 64)
    private String sn;

    /** 通讯状态：ONLINE / OFFLINE */
    @Column(nullable = false, length = 16)
    private String status;

    /** 额定功率阈值 Pmax (W) */
    @Column(nullable = false)
    private Double pmax;

    /** 外键：所属建筑 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    /** 房间号（如：301、101） */
    @Column(name = "room_no", nullable = false, length = 32)
    private String roomNo;

    /** 创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 是否有效：1有效 / 0停用或拆除 */
    @Column(nullable = false)
    private Boolean active;

    /** 一个设备可以有多个能耗采集记录 */
    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
    private List<EnergyRecord> records = new ArrayList<>();

    /** 一个设备可以有多个告警记录 */
    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
    private List<AlarmRecord> alarms = new ArrayList<>();
}
