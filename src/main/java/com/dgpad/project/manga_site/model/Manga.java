package com.dgpad.project.manga_site.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mangas")
public class Manga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Integer communityScore;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    private Date releaseDate;

    private String description;

    @Column(name = "cover_image", length = 2048)  // Adjust the length
    private String coverImage;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0;
        }
        return ratings.stream().mapToDouble(Rating::getValue).average().orElse(0);
    }

//    public Chapter getLastChapterReleased() {
//        return chapters.stream()
//                .max(Comparator.comparing(Chapter::getReleaseDate))
//                .orElse(null);
//    }
}
