package com.dgpad.project.manga_site.controller;

import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("readMangaCount", user.getReadChapters().size());
        model.addAttribute("bookmarkedMangaCount", user.getBookmarkedMangas().size());
        return "user_profile";
    }

    @GetMapping("/edit")
    public String editProfile(Authentication authentication, Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute User updatedUser, Authentication authentication) {
        User currentUser = userService.getCurrentUser();
        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setEmail(updatedUser.getEmail());
        userService.save(currentUser);
        return "redirect:/user/profile";
    }
}