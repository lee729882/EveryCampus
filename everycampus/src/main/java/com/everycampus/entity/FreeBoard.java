package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String writer;
    @Column(nullable = false)
    private String school; // university → school로 변경 (DB 컬럼과 일치)

    private LocalDateTime createdAt = LocalDateTime.now();
    


}
