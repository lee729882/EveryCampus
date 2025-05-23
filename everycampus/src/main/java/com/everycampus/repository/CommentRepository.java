package com.everycampus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everycampus.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardIdOrderByCreatedAtAsc(Long boardId);
}
