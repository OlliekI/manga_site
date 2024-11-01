package com.dgpad.project.manga_site.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bookmarked_mangas")
public class BookmarkedManga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    public BookmarkedManga(User user, Manga manga) {
        this.user = user;
        this.manga = manga;
    }
}
