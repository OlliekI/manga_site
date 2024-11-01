package com.dgpad.project.manga_site.controller;

import com.dgpad.project.manga_site.model.Manga;
import com.dgpad.project.manga_site.services.MangaService;
import com.dgpad.project.manga_site.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MangaService mangaService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        List<Manga> trendingMangas = mangaService.getTrendingMangas();
        List<Manga> editorsPicks = mangaService.getEditorsPicks();
        List<Manga> communitySpotlight = mangaService.getCommunitySpotlight();
        List<Manga> upcomingReleases = mangaService.getUpcomingReleases();

        model.addAttribute("trendingMangas", trendingMangas);
        model.addAttribute("editorsPicks", editorsPicks);
        model.addAttribute("communitySpotlight", communitySpotlight);
        model.addAttribute("upcomingReleases", upcomingReleases);

        return "home";
    }
}
