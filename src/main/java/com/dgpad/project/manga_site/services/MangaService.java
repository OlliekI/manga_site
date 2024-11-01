package com.dgpad.project.manga_site.services;

import com.dgpad.project.manga_site.model.Manga;
import com.dgpad.project.manga_site.model.Rating;
import com.dgpad.project.manga_site.model.User;
import com.dgpad.project.manga_site.repository.MangaRepository;
import com.dgpad.project.manga_site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MangaService {
    @Autowired
    private MangaRepository mangaRepository;

    @Autowired
    private UserRepository userRepository;

    public Iterable<Manga> findAll() {
        return mangaRepository.findAll();
    }

    public List<Manga> findAllLatest() {
        return mangaRepository.findAllByOrderByCreatedDateDesc();
    }

    public Manga findById(Long id) {
        return mangaRepository.findById(id).orElse(null);
    }

    public Manga save(Manga manga) {
        return mangaRepository.save(manga);
    }

    public Page<Manga> findAllPaginated(int page, int size) {
        return mangaRepository.findAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }

    public List<Manga> searchManga(String query) {
        return mangaRepository.searchManga(query);
    }

    public void rateManga(Long mangaId, Long userId, int rating) {
        Manga manga = mangaRepository.findById(mangaId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Rating existingRating = manga.getRatings().stream()
                .filter(r -> r.getUser().getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (existingRating != null) {
            existingRating.setValue(rating);
        } else {
            Rating newRating = new Rating();
            newRating.setManga(manga);
            newRating.setUser(user);
            newRating.setValue(rating);
            manga.getRatings().add(newRating);
        }

        mangaRepository.save(manga);
    }

    public List<Manga> getTrendingMangas() {
        return mangaRepository.findTop10ByOrderByRatingDesc();
    }

    public List<Manga> getEditorsPicks() {
        return mangaRepository.findTop10ByOrderByCreatedDateDesc();
    }

    public List<Manga> getCommunitySpotlight() {
        return mangaRepository.findTop10ByOrderByCommunityScoreDesc();
    }

    public List<Manga> getUpcomingReleases() {
        return mangaRepository.findUpcomingReleases();
    }

}
