package com.everycampus.repository;

import com.everycampus.entity.BoardLike;
import com.everycampus.entity.BoardLikeId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardLikeId> {

    @Query("SELECT COUNT(b) > 0 FROM BoardLike b WHERE b.postId = :postId AND b.username = :username")
    boolean existsByPostIdAndUsername(@Param("postId") Long postId, @Param("username") String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM BoardLike b WHERE b.postId = :postId AND b.username = :username")
    void deleteByPostIdAndUsername(@Param("postId") Long postId, @Param("username") String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM BoardLike b WHERE b.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId); // 게시글 삭제 시 모든 좋아요 삭제
}
