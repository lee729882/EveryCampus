package com.everycampus.controller;

import com.everycampus.entity.BoardLike;
import com.everycampus.entity.FreeBoard;
import com.everycampus.entity.User;
import com.everycampus.repository.BoardLikeRepository;
import com.everycampus.repository.FreeBoardRepository;
import com.everycampus.repository.UserRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Builder // 이 줄이 꼭 필요함
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FreeBoard> create(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("writer") String writer,
            @RequestParam("school") String school,
            @RequestParam("category") String category,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Optional<User> optionalUser = userRepository.findByUsername(writer);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path path = Paths.get("uploads", filename);
                Files.createDirectories(path.getParent());
                Files.write(path, image.getBytes());
                imageUrl = "/uploads/" + filename;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        FreeBoard post = FreeBoard.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .school(school)
                .category(category)
                .createdAt(LocalDateTime.now())
                .likeCount(0)
                .imageUrl(imageUrl)
                .build();

        return ResponseEntity.ok(freeBoardRepository.save(post));
    }


    // 게시글 수정
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("school") String school,
            @RequestParam("category") String category,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        Optional<FreeBoard> optional = freeBoardRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        post.setTitle(title);
        post.setContent(content);
        post.setSchool(school);
        post.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path path = Paths.get("uploads", filename);
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                post.setImageUrl("/uploads/" + filename); // 이미지 URL 업데이트
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 실패");
            }
        }

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
    
    // HOT 게시물
    @GetMapping("/hot")
    public List<FreeBoard> getHotPosts() {
        return freeBoardRepository.findTop10ByLikeCountGreaterThanOrderByLikeCountDesc(0);
    }

}
