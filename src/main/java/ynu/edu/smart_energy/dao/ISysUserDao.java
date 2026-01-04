package ynu.edu.smart_energy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ynu.edu.smart_energy.entity.SysUser;

import java.util.Optional;

public interface ISysUserDao extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByUsername(String username);
}
