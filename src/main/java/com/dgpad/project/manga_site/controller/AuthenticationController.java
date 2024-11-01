package com.dgpad.project.manga_site.controller;

import com.dgpad.project.manga_site.model.Role;
import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String email,
                                 @RequestParam String role) {
        if (userService.existsByUsername(username)) {
            return new ModelAndView("redirect:/auth/register?error=usernameTaken");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        if ("PUBLISHER".equalsIgnoreCase(role)) {
            user.setRole(Role.PUBLISHER);
        } else {
            user.setRole(Role.USER);
        }

        userService.save(user);
        return new ModelAndView("redirect:/auth/login");
    }
}
