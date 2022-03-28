package academy.kata.platform.spring.spring_boot_rest.dao;

import academy.kata.platform.spring.spring_boot_rest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/*Если интерфейс расширяет JpaRepository, то базовые методы на получение,
удаление и т.д. из БД реальзовывать НЕ нужно, они уже написаны за нас.

Если нам нужен свой метод для работы с БД, которого нет в JpaRepository, то следуя правилам написания имени метода,
Spring также сам его реализует!
*/
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByRole(String roleName);

    Role findRoleById(long id);

}
