package com.everycampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everycampus.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}