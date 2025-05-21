package com.everycampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.everycampus.entity.FreeBoard;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {}
