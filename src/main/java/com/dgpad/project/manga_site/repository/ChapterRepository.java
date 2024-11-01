package com.dgpad.project.manga_site.repository;

import com.dgpad.project.manga_site.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Chapter findTopByMangaIdOrderByChapterNumberDesc(Long mangaId);
}
