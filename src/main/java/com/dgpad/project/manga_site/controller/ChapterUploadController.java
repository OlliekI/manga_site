package com.dgpad.project.manga_site.controller;

import com.dgpad.project.manga_site.model.Chapter;
import com.dgpad.project.manga_site.model.Manga;
import com.dgpad.project.manga_site.services.ChapterService;
import com.dgpad.project.manga_site.services.MangaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/manga/{mangaId}/chapter/upload")
public class ChapterUploadController {

    @Autowired
    private MangaService mangaService;

    @Autowired
    private ChapterService chapterService;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public String showUploadChapterPage(@PathVariable Long mangaId, Model model, Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Manga manga = mangaService.findById(mangaId);

        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PUBLISHER")) &&
                manga.getAuthor().equals(user.getUsername())) {
            model.addAttribute("chapter", new Chapter());
            model.addAttribute("manga", manga);
            return "upload_chapter";
        } else {
            return "403";
        }
    }

    @PostMapping
    public String uploadChapter(@PathVariable Long mangaId,
                                @RequestParam("title") String title,
                                @RequestParam("chapterNumber") Integer chapterNumber,
                                @RequestParam("imageUrls") String imageUrls) {
        Manga manga = mangaService.findById(mangaId);
        Chapter chapter = new Chapter();
        chapter.setTitle(title);
        chapter.setChapterNumber(chapterNumber);
        chapter.setManga(manga);


        List<String> urls = Arrays.asList(imageUrls.split("\\s*,\\s*"));
        chapter.setImageUrls(urls);

        chapterService.save(chapter);

        return "redirect:/manga/" + mangaId;
    }
}
