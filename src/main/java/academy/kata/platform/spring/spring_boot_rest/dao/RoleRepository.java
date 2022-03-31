package academy.kata.platform.spring.spring_boot_rest.dao;

import academy.kata.platform.spring.spring_boot_rest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByRole(String roleName);

    Role findRoleById(long id);

}
