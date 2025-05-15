package com.everycampus.controller;

import com.everycampus.entity.User;
import com.everycampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 아이디입니다.");
        }

        userRepository.save(user);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }


}