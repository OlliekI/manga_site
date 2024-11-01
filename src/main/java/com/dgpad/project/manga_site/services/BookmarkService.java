package com.dgpad.project.manga_site.services;

import com.dgpad.project.manga_site.model.BookmarkedManga;
import com.dgpad.project.manga_site.model.Manga;
import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.repository.BookmarkRepository;
import com.dgpad.project.manga_site.repository.MangaRepository;
import com.dgpad.project.manga_site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MangaRepository mangaRepository;

    public void addBookmark(Long userId, Long mangaId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Manga manga = mangaRepository.findById(mangaId).orElseThrow(() -> new RuntimeException("Manga not found"));

        Optional<BookmarkedManga> existingBookmark = bookmarkRepository.findByUserIdAndMangaId(userId, mangaId);
        if (existingBookmark.isEmpty()) {
            BookmarkedManga bookmarkedManga = new BookmarkedManga(user, manga);
            bookmarkRepository.save(bookmarkedManga);
        }
    }

    public void removeBookmark(Long userId, Long mangaId) {
        Optional<BookmarkedManga> bookmark = bookmarkRepository.findByUserIdAndMangaId(userId, mangaId);
        bookmark.ifPresent(bookmarkRepository::delete);
    }

    public Iterable<BookmarkedManga> getBookmarkedMangas(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    public boolean isBookmarked(Long userId, Long mangaId) {
        return bookmarkRepository.findByUserIdAndMangaId(userId, mangaId).isPresent();
    }
}
