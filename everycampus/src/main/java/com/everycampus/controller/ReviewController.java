package com.everycampus.controller;

import com.everycampus.entity.Review;
import com.everycampus.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewRepository reviewRepository;

    // 과목별 리뷰 페이지
    @GetMapping("/lecture")
    public String viewLectureReviews(@RequestParam("name") String lectureName, Model model) {
        List<Review> reviews = reviewRepository.findByLectureName(lectureName);
        model.addAttribute("lectureName", lectureName);
        model.addAttribute("reviews", reviews);
        return "review/lecture-review";
    }

    // 전체 리뷰 최신순 보기
    @GetMapping("/all")
    public String viewAllReviews(Model model) {
        List<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("reviews", reviews);
        return "review/all-reviews";
    }

    // 리뷰 저장 처리
    @PostMapping("/save")
    public String saveReview(@ModelAttribute Review review) {
        reviewRepository.save(review);
        return "redirect:/review/lecture?name=" + review.getLectureName();
    }
}
