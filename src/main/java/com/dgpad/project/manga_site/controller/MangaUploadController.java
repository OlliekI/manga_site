package com.dgpad.project.manga_site.controller;

import com.dgpad.project.manga_site.model.Manga;
import com.dgpad.project.manga_site.model.Role;
import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.services.MangaService;
import com.dgpad.project.manga_site.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/manga")
public class MangaUploadController {

    @Autowired
    private MangaService mangaService;

    @Autowired
    private UserService userService;

    @GetMapping("/upload")
    public String showUploadPage(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getRole() == Role.PUBLISHER) {
            model.addAttribute("manga", new Manga());
            return "upload_manga";
        } else {
            return "403";
        }
    }

    @PostMapping("/upload")
    public String uploadManga(Manga manga, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        if (user.getRole() == Role.PUBLISHER) {
            manga.setAuthor(user.getUsername());
            manga.setCreatedDate(LocalDateTime.now());
            mangaService.save(manga);
            return "redirect:/manga/latest";
        } else {
            return "403";
        }
    }
}
