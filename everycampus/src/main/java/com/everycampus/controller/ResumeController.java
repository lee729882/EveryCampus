package com.everycampus.controller;

import com.everycampus.entity.Resume;
import com.everycampus.repository.ResumeRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeRepository resumeRepository;

    @GetMapping("/resume")
    public String showForm(Model model) {
        model.addAttribute("resume", new Resume());
        return "resume_form";  // templates/resume_form.html을 렌더링함
    }

    @PostMapping("/resume")
    public String saveResume(@ModelAttribute Resume resume, HttpSession session) {
        String username = (String) session.getAttribute("username");
        resume.setUsername(username);
        resumeRepository.save(resume);
        return "redirect:/resume/view";
    }

    @GetMapping("/resume/view")
    public String viewResume(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Resume resume = resumeRepository.findTopByUsernameOrderByIdDesc(username);
        model.addAttribute("resume", resume);
        return "resume_view";
    }
}
