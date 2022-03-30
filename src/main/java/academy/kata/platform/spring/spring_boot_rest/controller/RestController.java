package academy.kata.platform.spring.spring_boot_rest.controller;

import academy.kata.platform.spring.spring_boot_rest.exception.api.NoSuchUserException;
import academy.kata.platform.spring.spring_boot_rest.model.Role;
import academy.kata.platform.spring.spring_boot_rest.model.User;
import academy.kata.platform.spring.spring_boot_rest.service.RoleService;
import academy.kata.platform.spring.spring_boot_rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

//@RestController - это Controller оторый управляет REST запросами и ответами.
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RestController(UserService userService, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/roles")
    public List<Role> allRoles() {
        List<Role> roles = roleService.getAllRoles();
        return roles;
    }

    @GetMapping("/auth")
    public User authUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        if (user == null) {
            throw new NoSuchUserException("Запрашиваемый User с Username = " + auth.getName() + " не найден!");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        List<User> users = userService.getAllUsers();
        return users;
    }

    @PostMapping("/users")
    public User saveNewUser(@ModelAttribute("userAttribute") User user) {

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setUsername("user_" + (int)(Math.random() * 10000001));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles());

        userService.saveOrUpdate(user);
        return user;

    }

    @PatchMapping("/users")
    public User updateUser(@ModelAttribute("userAttribute") User user) {
        User userBd = userService.findUserById(user.getId());

        user.setUsername(userBd.getUsername());
        user.setAccountNonExpired(userBd.isAccountNonExpired());
        user.setAccountNonLocked(userBd.isAccountNonLocked());
        user.setCredentialsNonExpired(userBd.isCredentialsNonExpired());
        user.setEnabled(userBd.isEnabled());
        if (user.getPassword().equals("")) {
            user.setPassword(userBd.getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (user.getRoles() == null) {
            user.setRoles(Collections.singleton(roleService.findRoleByRole("ROLE_USER")));
        }

        userService.saveOrUpdate(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String removeUser(@PathVariable("id") long id) {
        if (userService.findUserById(id) == null) {
            throw new NoSuchUserException("Запрашиваемый User с ID = " + id + " не найден!");
        }
        userService.removeUserById(id);
        return "User c ID = " + id + " был удален!";
    }

}
