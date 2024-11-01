package com.dgpad.project.manga_site.controller;

import com.dgpad.project.manga_site.model.BookmarkedManga;
import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.services.BookmarkService;
import com.dgpad.project.manga_site.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showBookmarkedMangas(Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            Iterable<BookmarkedManga> bookmarkedMangas = bookmarkService.getBookmarkedMangas(currentUser.getId());
            model.addAttribute("bookmarkedMangas", bookmarkedMangas);
        } else {
            return "redirect:/auth/login";
        }
        return "bookmarks";
    }


}

