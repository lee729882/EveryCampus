package com.everycampus.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everycampus.repository.FreeBoardRepository;
import com.everycampus.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everycampus.entity.FreeBoard;
import com.everycampus.entity.User;
import com.everycampus.repository.TimetableRepository; // 추가


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    private final FreeBoardRepository freeBoardRepository;

    private final TimetableRepository timetableRepository;

    public AdminController(UserRepository userRepository,
            FreeBoardRepository freeBoardRepository,
            TimetableRepository timetableRepository) {
		this.userRepository = userRepository;
		this.freeBoardRepository = freeBoardRepository;
		this.timetableRepository = timetableRepository;
	}



    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
        timetableRepository.deleteByUserId(id);

        userRepository.deleteById(id);
        return ResponseEntity.ok("삭제 완료");
    }
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<FreeBoard> posts = freeBoardRepository.findAll();

        List<Map<String, Object>> result = posts.stream().map(post -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", post.getId());
            map.put("title", post.getTitle());
            map.put("writer", post.getWriter());
            map.put("school", post.getSchool());

            // ✅ 게시판 이름 한글로 매핑
            String boardType = switch (post.getCategory()) {
                case "secret" -> "비밀 게시판";
                case "free" -> "자유 게시판";
                case "global" -> "통합 게시판";
                case "graduate" -> "졸업자 게시판";
                default -> "알 수 없음";
            };

            map.put("category", boardType);
            map.put("content", post.getContent());
            map.put("createdAt", post.getCreatedAt());
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }



    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
        }
        freeBoardRepository.deleteById(id);
        return ResponseEntity.ok("삭제 완료");
    }
    
}