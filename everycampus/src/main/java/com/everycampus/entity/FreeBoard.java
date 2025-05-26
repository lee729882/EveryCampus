package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FreeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String writer;
    private String imageUrl; // 이미지 파일 경로를 저장할 필드


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

    // 댓글과의 연관관계 설정 (양방향) - 댓글달린 게시물 삭제되자 않는 문제 해결책
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
