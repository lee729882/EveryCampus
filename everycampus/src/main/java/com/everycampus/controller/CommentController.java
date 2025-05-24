package com.everycampus.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everycampus.entity.Comment;
import com.everycampus.entity.FreeBoard;
import com.everycampus.repository.CommentRepository;
import com.everycampus.repository.FreeBoardRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final FreeBoardRepository freeBoardRepository;

    @PostMapping("/{boardId}")
    public ResponseEntity<?> addComment(@PathVariable("boardId") Long boardId, @RequestBody Comment comment) {
        Optional<FreeBoard> boardOpt = freeBoardRepository.findById(boardId);
        if (boardOpt.isEmpty()) return ResponseEntity.notFound().build();

        comment.setBoard(boardOpt.get());
        comment.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @GetMapping("/{boardId}")
    public List<Comment> getComments(@PathVariable("boardId") Long boardId) {
        return commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id,
                                           @RequestParam("username") String username,
                                           @RequestBody Comment updated) {
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Comment comment = opt.get();

        if (!comment.getWriter().equals(username))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한 없음");

        comment.setContent(updated.getContent());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id,
                                           @RequestParam("username") String username) {
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Comment comment = opt.get();

        if (!comment.getWriter().equals(username))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한 없음");

        commentRepository.delete(comment);
        return ResponseEntity.ok().build();
    }
}
