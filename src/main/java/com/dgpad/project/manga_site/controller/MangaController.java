package com.dgpad.project.manga_site.controller;

import com.dgpad.project.manga_site.model.Chapter;
import com.dgpad.project.manga_site.model.Manga;
import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.services.BookmarkService;
import com.dgpad.project.manga_site.services.ChapterService;
import com.dgpad.project.manga_site.services.MangaService;
import com.dgpad.project.manga_site.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manga")
public class MangaController {

    @Autowired
    private MangaService mangaService;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/latest")
    public String latestManga(Model model) {
        List<Manga> latestMangas = mangaService.findAllLatest();
        model.addAttribute("latestMangas", latestMangas);
        return "latest";
    }


    @GetMapping("/browse")
    public String browseMangas(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        // Fetch the paginated mangas
        Page<Manga> mangaPage = mangaService.findAllPaginated(page, size);

        // Get the current logged-in user
        User currentUser = userService.getUserByUsername(userDetails.getUsername());

        // Map to hold last read and last released chapter for each manga
        Map<Long, Chapter> lastReadChapters = new HashMap<>();
        Map<Long, Chapter> lastReleasedChapters = new HashMap<>();

        // Loop through each manga to get the required chapters
        for (Manga manga : mangaPage.getContent()) {
            // Get the last released chapter for this manga
            Chapter lastReleased = chapterService.findLastReleasedChapter(manga.getId());
            lastReleasedChapters.put(manga.getId(), lastReleased);

            // Get the last read chapter for this user
            Chapter lastRead = userService.findLastReadChapter(currentUser, manga);
            lastReadChapters.put(manga.getId(), lastRead);
        }

        // Add all attributes to the model
        model.addAttribute("mangas", mangaPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", mangaPage.getTotalPages());
        model.addAttribute("lastReadChapters", lastReadChapters);
        model.addAttribute("lastReleasedChapters", lastReleasedChapters);

        return "browse";
    }


    @GetMapping("/history")
    public String getHistory(Model model) {
        User currentUser = userService.getCurrentUser();
        Iterable<Chapter> readChapters = userService.getReadChapters(currentUser.getId());
        model.addAttribute("readChapters", readChapters);
        return "history"; // Thymeleaf template name
    }

    @PostMapping("/bookmark")
    public String addOrRemoveBookmark(@RequestParam Long mangaId, @RequestParam(required = false) Boolean remove) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            if (remove != null && remove) {
                bookmarkService.removeBookmark(currentUser.getId(), mangaId);
            } else {
                bookmarkService.addBookmark(currentUser.getId(), mangaId);
            }
        }
        return "redirect:/manga/" + mangaId;
    }


    @GetMapping("/{id}")
    public String viewManga(@PathVariable Long id, Model model) {
        Manga manga = mangaService.findById(id);
        if (manga != null) {
            DecimalFormat df = new DecimalFormat("#.#");
            String formattedRating = df.format(manga.getAverageRating());
            model.addAttribute("manga", manga);
            model.addAttribute("formattedRating", formattedRating);

            User currentUser = userService.getCurrentUser();
            boolean isBookmarked = currentUser != null && bookmarkService.isBookmarked(currentUser.getId(), manga.getId());
            model.addAttribute("isBookmarked", isBookmarked);

            return "viewManga";
        } else {
            return "404";
        }
    }

    @GetMapping("/chapter/{id}")
    public String viewChapter(@PathVariable Long id, Model model) {
        Chapter chapter = chapterService.findById(id);
        if (chapter != null) {
            Manga manga = mangaService.findById(chapter.getManga().getId());
            if (manga != null) {
                // Ensure the manga's chapters are correctly retrieved
                List<Chapter> chapters = manga.getChapters();
                int chapterIndex = chapters.indexOf(chapter);

                // Determine previous and next chapter IDs
                Long previousChapterId = (chapterIndex > 0) ? chapters.get(chapterIndex - 1).getId() : null;
                Long nextChapterId = (chapterIndex < chapters.size() - 1) ? chapters.get(chapterIndex + 1).getId() : null;

                // Add attributes to the model
                model.addAttribute("manga", manga);
                model.addAttribute("chapter", chapter);
                model.addAttribute("previousChapterId", previousChapterId);
                model.addAttribute("nextChapterId", nextChapterId);

                // Update read history
                User currentUser = userService.getCurrentUser();
                userService.addToReadHistory(currentUser.getId(), chapter.getId());

                return "viewChapter";
            }
        }
        return "404";
    }

}

