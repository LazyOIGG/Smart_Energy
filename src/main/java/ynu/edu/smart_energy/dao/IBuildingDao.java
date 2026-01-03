package ynu.edu.smart_energy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ynu.edu.smart_energy.entity.Building;

public interface IBuildingDao extends JpaRepository<Building, Long> {
}
