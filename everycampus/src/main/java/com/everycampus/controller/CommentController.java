package com.everycampus.controller;

import com.everycampus.entity.Comment;
import com.everycampus.entity.FreeBoard;
import com.everycampus.repository.CommentRepository;
import com.everycampus.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final FreeBoardRepository freeBoardRepository;

    @PostMapping("/{boardId}")
    public ResponseEntity<?> addComment(@PathVariable("boardId") Long boardId,
                                        @RequestBody Comment comment) {
        Optional<FreeBoard> boardOpt = freeBoardRepository.findById(boardId);
        if (boardOpt.isEmpty()) return ResponseEntity.notFound().build();

        comment.setPost(boardOpt.get()); // 엔티티 필드명 post로 연결
        comment.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    // 댓글 조회
    @GetMapping("/{boardId}")
    public List<Comment> getComments(@PathVariable("boardId") Long boardId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(boardId);  // post.id 사용
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestParam String username,
                                           @RequestBody Comment updatedComment) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Comment comment = optional.get();
        if (!comment.getWriter().equals(username)) {
            return ResponseEntity.status(403).body("작성자만 수정할 수 있습니다.");
        }

        comment.setContent(updatedComment.getContent());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @RequestParam String username) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Comment comment = optional.get();
        if (!comment.getWriter().equals(username)) {
            return ResponseEntity.status(403).body("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
        return ResponseEntity.ok("삭제 완료");
    }
}
