package com.everycampus.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board_like")
@IdClass(BoardLikeId.class)
public class BoardLike {

    @Id
    private Long postId;

    @Id
    private String username;

    private LocalDateTime likedAt;

    // ✅ postId, username으로 생성 시 likedAt은 현재 시간으로 설정
    public BoardLike(Long postId, String username) {
        this.postId = postId;
        this.username = username;
        this.likedAt = LocalDateTime.now();
    }
}
