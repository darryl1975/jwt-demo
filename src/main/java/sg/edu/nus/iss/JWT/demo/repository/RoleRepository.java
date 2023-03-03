package sg.edu.nus.iss.JWT.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.JWT.demo.model.ERole;
import sg.edu.nus.iss.JWT.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
