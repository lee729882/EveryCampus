package com.everycampus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everycampus.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, String> {
    Optional<Lecture> findByLectureName(String lectureName);
}
