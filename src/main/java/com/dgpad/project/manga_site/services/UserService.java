package com.dgpad.project.manga_site.services;

import com.dgpad.project.manga_site.model.Manga;
import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.model.Chapter;
import com.dgpad.project.manga_site.repository.ChapterRepository;
import com.dgpad.project.manga_site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    public Iterable<Chapter> getReadChapters(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getReadChapters();
    }

    public Chapter findLastReadChapter(User user, Manga manga) {
        return user.getReadChapters().stream()
                .filter(chapter -> chapter.getManga().equals(manga))
                .max(Comparator.comparing(Chapter::getChapterNumber)) // Assuming chapterNumber is an Integer
                .orElse(null); // Return null if no chapter is found
    }

    public void addToReadHistory(Long userId, Long chapterId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new RuntimeException("Chapter not found"));

        // Check if the manga associated with the chapter is already in the read history
        boolean found = false;
        for (Chapter readChapter : user.getReadChapters()) {
            if (readChapter.getManga().equals(chapter.getManga())) {
                // Update the chapter in the history
                readChapter.setId(chapterId);
                chapterRepository.save(readChapter);  // Make sure to save the updated chapter
                found = true;
                break;
            }
        }

        if (!found) {
            user.getReadChapters().add(chapter); // Add new chapter to the history if not found
        }

        userRepository.save(user);
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("No authenticated user found");
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
