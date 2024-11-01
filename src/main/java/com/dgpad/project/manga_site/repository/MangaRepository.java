package com.dgpad.project.manga_site.repository;

import com.dgpad.project.manga_site.model.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MangaRepository extends JpaRepository<Manga, Long> {
    List<Manga> findAllByOrderByCreatedDateDesc();

    @Query("SELECT m FROM Manga m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.author) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Manga> searchManga(@Param("query") String query);

    @Query("SELECT m FROM Manga m JOIN Rating r ON m.id = r.manga.id GROUP BY m.id ORDER BY AVG(r.value) DESC")
    List<Manga> findTop10ByOrderByRatingDesc(); // Trending Manga - Most Rated
    List<Manga> findTop10ByOrderByCreatedDateDesc(); // Editor’s Picks - Most Recent
    List<Manga> findTop10ByOrderByCommunityScoreDesc(); // Community Spotlight - Most Liked
    @Query("SELECT m FROM Manga m WHERE m.releaseDate > CURRENT_DATE")
    List<Manga> findUpcomingReleases(); // Placeholder method for upcoming releases
}
