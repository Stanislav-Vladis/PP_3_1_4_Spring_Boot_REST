package academy.kata.platform.spring.spring_boot_rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @GetMapping("/user")
    public String authUser() {
        return "rests-show";
    }

    @GetMapping("/admin")
    public String allAdmin() {
        return "rests-show";
    }

}
