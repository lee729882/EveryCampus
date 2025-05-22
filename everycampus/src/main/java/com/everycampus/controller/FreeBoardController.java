package com.everycampus.controller;

import com.everycampus.entity.FreeBoard;
import com.everycampus.entity.User;
import com.everycampus.repository.FreeBoardRepository;
import com.everycampus.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/board/free")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardRepository freeBoardRepository;
    private final UserRepository userRepository;


    // 전체 게시글 조회
    @GetMapping
    public List<FreeBoard> list() {
        return freeBoardRepository.findAll();
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<FreeBoard> create(@RequestBody FreeBoard board) {
        Optional<User> optionalUser = userRepository.findByUsername(board.getWriter());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // school 값을 사용자에게서 가져와 저장
        board.setSchool(optionalUser.get().getSchool());
        board.setCreatedAt(LocalDateTime.now());

        FreeBoard saved = freeBoardRepository.save(board);
        return ResponseEntity.ok(saved);
    }



    //게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
            @RequestParam("username") String username) {
        Optional<FreeBoard> optional = freeBoardRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        freeBoardRepository.delete(post);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
    			@RequestParam("username") String username,
    			@RequestBody FreeBoard updatedPost) {
        Optional<FreeBoard> optional = freeBoardRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoard post = optional.get();

        // 작성자 검증
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        // 내용 수정
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());

        freeBoardRepository.save(post);
        return ResponseEntity.ok(post);
    }


}
