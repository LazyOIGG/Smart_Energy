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
@Table(name = "building")
public class Building {

    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 建筑名称（如：力行楼、楸苑宿舍三号楼） */
    @Column(nullable = false, length = 64)
    private String name;

    /** 位置编号（如：BLD01 / QIU_03） */
    @Column(name = "location_code", length = 64)
    private String locationCode;

    /** 楼层数 */
    private Integer floors;

    /** 建筑用途分类（宿舍/教学楼/实验楼/图书馆等） */
    @Column(name = "usage_type", length = 64)
    private String usageType;

    /** 创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 一个建筑可以有多个电表设备 */
    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    private List<MeterDevice> devices = new ArrayList<>();
}
