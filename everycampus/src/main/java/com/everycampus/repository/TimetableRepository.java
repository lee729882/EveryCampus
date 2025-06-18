package com.everycampus.repository;

import com.everycampus.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByStudentId(String studentId);
    void deleteByStudentId(String studentId);
    void deleteByUserId(Long userId);

}
