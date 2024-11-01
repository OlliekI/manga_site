package com.dgpad.project.manga_site.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer chapterNumber;

    private LocalDateTime releaseDate;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;


    @ElementCollection
    private List<String> imageUrls;

    @ManyToMany(mappedBy = "readChapters")
    private List<User> users;
}
