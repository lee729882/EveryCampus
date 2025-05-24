package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FreeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String writer;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String category;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @ElementCollection
    @CollectionTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "username")
    private Set<String> likedUsers = new HashSet<>();
}
