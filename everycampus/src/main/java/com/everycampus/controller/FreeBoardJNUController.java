package com.everycampus.controller;

import com.everycampus.entity.FreeBoard;
import com.everycampus.entity.FreeBoardJNU;
import com.everycampus.entity.User;
import com.everycampus.repository.FreeBoardJNURepository;
import com.everycampus.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/board/free-g") // 전남대 전용 API
@RequiredArgsConstructor
public class FreeBoardJNUController {

    private final FreeBoardJNURepository jnurepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<FreeBoardJNU> list() {
        return jnurepository.findAll();
    }

    @PostMapping
    public ResponseEntity<FreeBoardJNU> create(@RequestBody FreeBoardJNU board) {
        Optional<User> optionalUser = userRepository.findByUsername(board.getWriter());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        board.setSchool(optionalUser.get().getSchool());
        board.setCreatedAt(LocalDateTime.now());

        FreeBoardJNU saved = jnurepository.save(board);
        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @RequestHeader("username") String username) {
        Optional<FreeBoardJNU> optionalPost = jnurepository.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FreeBoardJNU post = optionalPost.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        jnurepository.delete(post);
        return ResponseEntity.ok().build();
    }

}
