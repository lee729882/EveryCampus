// ✅ CommentRepository.java
package com.everycampus.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.everycampus.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);  // post는 엔티티의 필드명
}