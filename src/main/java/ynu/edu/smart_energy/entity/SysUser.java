package ynu.edu.smart_energy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * ADMIN / USER
     */
    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Boolean enabled = true;

    private LocalDateTime createdAt;
}
