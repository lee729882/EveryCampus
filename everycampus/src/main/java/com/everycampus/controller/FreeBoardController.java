package com.everycampus.controller;

import com.everycampus.entity.BoardLike;
import com.everycampus.entity.FreeBoard;
import com.everycampus.entity.User;
import com.everycampus.repository.BoardLikeRepository;
import com.everycampus.repository.FreeBoardRepository;
import com.everycampus.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardRepository freeBoardRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;

    // 게시글 목록 조회
    @GetMapping
    public List<FreeBoard> list(
            @RequestParam(name = "school", required = false) String school,
            @RequestParam(name = "category") String category
    ) {
        if (school != null) {
            return freeBoardRepository.findBySchoolAndCategoryOrderByCreatedAtDesc(school, category);
        } else {
            return freeBoardRepository.findByCategoryOrderByCreatedAtDesc(category);
        }
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<FreeBoard> create(@RequestBody FreeBoard board) {
        Optional<User> optionalUser = userRepository.findByUsername(board.getWriter());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        board.setSchool(optionalUser.get().getSchool());
        board.setCreatedAt(LocalDateTime.now());
        board.setLikeCount(0);  // 기본값 설정
        FreeBoard saved = freeBoardRepository.save(board);
        return ResponseEntity.ok(saved);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestParam("username") String username,
                                    @RequestBody FreeBoard updatedPost) {
        Optional<FreeBoard> optional = freeBoardRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        freeBoardRepository.save(post);
        return ResponseEntity.ok(post);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @RequestParam("username") String username) {
        Optional<FreeBoard> optional = freeBoardRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 게시글 삭제 시 좋아요 기록도 삭제 (옵션)
        boardLikeRepository.deleteByPostId(id);

        freeBoardRepository.delete(post);
        return ResponseEntity.ok().build();
    }

    // 좋아요 토글 (누르면 추가, 다시 누르면 취소)
    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("id") Long postId,
                                        @RequestParam("username") String username) {
        Optional<FreeBoard> optionalPost = freeBoardRepository.findById(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optionalPost.get();
        boolean isLiked = boardLikeRepository.existsByPostIdAndUsername(postId, username);

        if (isLiked) {
            boardLikeRepository.deleteByPostIdAndUsername(postId, username);
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        } else {
            boardLikeRepository.save(new BoardLike(postId, username));
            post.setLikeCount(post.getLikeCount() + 1);
        }

        freeBoardRepository.save(post);

        return ResponseEntity.ok(Map.of(
                "isLiked", !isLiked,
                "likeCount", post.getLikeCount()
        ));
    }

    // 좋아요 상태 확인
    @GetMapping("/{id}/like")
    public ResponseEntity<?> getLikeStatus(@PathVariable("id") Long postId,
                                           @RequestParam("username") String username) {
        Optional<FreeBoard> optionalPost = freeBoardRepository.findById(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optionalPost.get();
        boolean isLiked = boardLikeRepository.existsByPostIdAndUsername(postId, username);

        return ResponseEntity.ok(Map.of(
                "isLiked", isLiked,
                "likeCount", post.getLikeCount()
        ));
    }
}
