package com.everycampus.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Id;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FREE_BOARD_GLOBAL")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String writer;
    private String school;

    private LocalDateTime createdAt = LocalDateTime.now();
}
