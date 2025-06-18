package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // 보기 내용

    private int count = 0;  // 선택된 횟수

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;
}
