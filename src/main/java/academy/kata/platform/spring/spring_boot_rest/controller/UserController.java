package academy.kata.platform.spring.spring_boot_rest.controller;

import academy.kata.platform.spring.spring_boot_rest.model.Role;
import academy.kata.platform.spring.spring_boot_rest.model.User;
import academy.kata.platform.spring.spring_boot_rest.service.RoleService;
import academy.kata.platform.spring.spring_boot_rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/user")
    public String authUser(ModelMap model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("user", new User());
        model.addAttribute("authenticationUser", userService.findUserByUsername(authentication.getName()));
        model.addAttribute("checkAdminRole", false);

        return "show";

    }

    @GetMapping("/admin")
    public String allUsers(ModelMap model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<Role> authRoles = userService.findUserByUsername(authentication.getName()).getRoles();
        List<User> users = userService.getAllUsers();
        List<Role> roles = roleService.getAllRoles();

        boolean[] checkAdminRole = {false};
        authRoles.stream().forEach(role -> {
            if (role.getRole().equals("ROLE_ADMIN") && checkAdminRole[0] == false) {
                checkAdminRole[0] = true;
            }
        });

        model.addAttribute("user", new User());
        model.addAttribute("authenticationUser", userService.findUserByUsername(authentication.getName()));
        model.addAttribute("checkAdminRole", checkAdminRole[0]);
        model.addAttribute("listUsers", users);
        model.addAttribute("listRoles", roles);

        return "show";

    }

    @PostMapping("/admin")
    public String saveNewUser(@ModelAttribute("userAttribute") User user,
                              @RequestParam(value="userTypeRoles", required = false) Set<Role> roles) {

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setUsername("user_" + (int)(Math.random() * 10000001));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roles);

        userService.saveOrUpdate(user);
        return "redirect:/admin";
    }

    @PatchMapping("admin/{id}")
    public String updateUser(@ModelAttribute("userAttribute") User user,
                             @RequestParam(value="userTypeRoles", required = false) Set<Role> roles) {

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        if (user.getPassword().equals("")) {
            user.setPassword(userService.findUserById(user.getId()).getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (roles == null) {
            user.setRoles(Collections.singleton(roleService.findRoleByRole("ROLE_USER")));
        } else {
            user.setRoles(roles);
        }

        userService.saveOrUpdate(user);
        return "redirect:/admin";
    }

    @DeleteMapping("admin/{id}")
    public String removeUser(@PathVariable("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

}
