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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everycampus.entity.FreeBoardGlobal;
import com.everycampus.entity.User;
import com.everycampus.repository.FreeBoardGlobalRepository;
import com.everycampus.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board/global")
@RequiredArgsConstructor
public class FreeBoardGlobalController {

    private final FreeBoardGlobalRepository repository;
    private final UserRepository userRepository;

    @GetMapping
    public List<FreeBoardGlobal> list() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<FreeBoardGlobal> create(@RequestBody FreeBoardGlobal post) {
        Optional<User> user = userRepository.findByUsername(post.getWriter());
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        post.setSchool(user.get().getSchool());
        post.setCreatedAt(LocalDateTime.now());

        return ResponseEntity.ok(repository.save(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @RequestParam("username") String username) {
        Optional<FreeBoardGlobal> optional = repository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FreeBoardGlobal post = optional.get();
        if (!post.getWriter().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        repository.delete(post);
        return ResponseEntity.ok().build();
    }


}
