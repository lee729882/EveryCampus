package com.everycampus.repository;

import com.everycampus.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByLectureName(String lectureName);

    List<Review> findAllByOrderByCreatedAtDesc();
}
