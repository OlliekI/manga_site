package com.dgpad.project.manga_site.services;

import com.dgpad.project.manga_site.model.Chapter;
import com.dgpad.project.manga_site.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;

    public Chapter save(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public Iterable<Chapter> findAll() {
        return chapterRepository.findAll();
    }

    public Chapter findById(Long id) {
        return chapterRepository.findById(id).orElse(null);
    }

    public Chapter findLastReleasedChapter(Long mangaId) {
        return chapterRepository.findTopByMangaIdOrderByChapterNumberDesc(mangaId); // Assuming this query exists
    }
}

