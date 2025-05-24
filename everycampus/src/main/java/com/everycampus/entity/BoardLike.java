package com.everycampus.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(length = 1000)
    private String username;

    private LocalDateTime likedAt;

    public BoardLike(Long postId, String username) {
        this.postId = postId;
        this.username = username;
        this.likedAt = LocalDateTime.now();
    }
}

