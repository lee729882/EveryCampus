package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "free_board_jnu") // 전남대 전용 테이블
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardJNU {

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
