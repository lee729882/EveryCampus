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

    /**
     * 게시글 목록 조회
     * @param school 학교 이름 (선택)
     * @param category 게시판 카테고리 (필수) - 예: free, graduates, global
     */
    @GetMapping
    public List<FreeBoard> list(
            @RequestParam(name = "school", required = false) String school,
            @RequestParam(name = "category") String category
    ) {
        if ("global".equalsIgnoreCase(category)) {
            // 통합 게시판은 학교 구분 없이 전체 글 조회
            return freeBoardRepository.findByCategoryOrderByCreatedAtDesc(category);
        } else if (school != null) {
            // 일반 게시판은 학교 + 카테고리 기준
            return freeBoardRepository.findBySchoolAndCategoryOrderByCreatedAtDesc(school, category);
        }

        // fallback
        return Collections.emptyList();
    }

    /**
     * 게시글 작성
     */
    @PostMapping
    public ResponseEntity<FreeBoard> create(@RequestBody FreeBoard board) {
        Optional<User> optionalUser = userRepository.findByUsername(board.getWriter());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        board.setSchool(optionalUser.get().getSchool());  // DB상 정확한 학교명 입력
        board.setCreatedAt(LocalDateTime.now());
        board.setLikeCount(0);  // 초기 좋아요 수
        return ResponseEntity.ok(freeBoardRepository.save(board));
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam String username,
                                    @RequestBody FreeBoard updatedPost) {
        Optional<FreeBoard> optional = freeBoardRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        return ResponseEntity.ok(freeBoardRepository.save(post));
    }

    /**
     * 게시글 삭제 (+ 좋아요도 함께 삭제)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam String username) {
        Optional<FreeBoard> optional = freeBoardRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        boardLikeRepository.deleteByPostId(id);  // 좋아요 연관 삭제
        freeBoardRepository.delete(post);
        return ResponseEntity.ok().build();
    }

    /**
     * 좋아요 토글
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("id") Long postId,
                                        @RequestParam("username") String username) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();  // 🔥 터미널에 어떤 에러인지 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }


    /**
     * 좋아요 상태 조회
     */
    @GetMapping("/{id}/like")
    public ResponseEntity<?> getLikeStatus(@PathVariable Long postId,
                                           @RequestParam String username) {
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
