package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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
    private String school;   // ex) "Mokpo", "SCNU"

    @Column(nullable = false)
    private String category; // ex) "free(자유 게시판)", "secret(비밀게시판)", "graduates(졸업생게시판)", "global(통합게시판)"

    private LocalDateTime createdAt = LocalDateTime.now();
}
