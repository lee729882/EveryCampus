package com.everycampus.repository;

import com.everycampus.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
	Resume findTopByUsernameOrderByIdDesc(String username);
}
