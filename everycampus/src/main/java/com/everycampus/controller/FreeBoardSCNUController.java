package com.everycampus.controller;

import com.everycampus.entity.FreeBoard;
import com.everycampus.entity.FreeBoardSCNU;
import com.everycampus.entity.User;
import com.everycampus.repository.FreeBoardSCNURepository;
import com.everycampus.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/board/free-s")
@RequiredArgsConstructor
public class FreeBoardSCNUController {

    private final FreeBoardSCNURepository scnurepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<FreeBoardSCNU> list() {
        return scnurepository.findAll();
    }

 // 게시글 작성
    @PostMapping
    public ResponseEntity<FreeBoardSCNU> create(@RequestBody FreeBoardSCNU board) {
        Optional<User> optionalUser = userRepository.findByUsername(board.getWriter());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // school 값을 사용자에게서 가져와 저장
        board.setSchool(optionalUser.get().getSchool());
        board.setCreatedAt(LocalDateTime.now());

        FreeBoardSCNU saved = scnurepository.save(board);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @RequestHeader("username") String username) {
        Optional<FreeBoardSCNU> optional = scnurepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoardSCNU post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        scnurepository.delete(post);
        return ResponseEntity.ok().build();
    }

}
