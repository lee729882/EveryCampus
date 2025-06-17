package com.everycampus.controller;

import com.everycampus.entity.User;
import com.everycampus.entity.FreeBoard;
import com.everycampus.repository.UserRepository;
import com.everycampus.repository.FreeBoardRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final UserRepository userRepository;
    private final FreeBoardRepository freeBoardRepository;

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        // Optional 처리 (없으면 예외 or null로 처리 가능)
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login"; // 로그인 안 된 상태 등 예외 처리
        }

        model.addAttribute("user", user);

        List<FreeBoard> posts = freeBoardRepository.findByWriterOrderByCreatedAtDesc(username);
        model.addAttribute("posts", posts);

        return "profile";
    }
}
