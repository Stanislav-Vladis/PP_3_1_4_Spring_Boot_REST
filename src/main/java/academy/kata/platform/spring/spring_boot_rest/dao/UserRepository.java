package academy.kata.platform.spring.spring_boot_rest.dao;

import academy.kata.platform.spring.spring_boot_rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserById(long id);

}
