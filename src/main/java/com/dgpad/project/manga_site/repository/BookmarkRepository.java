package com.dgpad.project.manga_site.repository;

import com.dgpad.project.manga_site.model.BookmarkedManga;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookmarkRepository extends CrudRepository<BookmarkedManga, Long> {
    Optional<BookmarkedManga> findByUserIdAndMangaId(Long userId, Long mangaId);
    Iterable<BookmarkedManga> findByUserId(Long userId);
}
