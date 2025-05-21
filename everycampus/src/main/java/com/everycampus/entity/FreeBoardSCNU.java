package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "free_board_scnu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardSCNU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String writer;
    @Column
    private String school;  // 반드시 있어야 함!


    private LocalDateTime createdAt = LocalDateTime.now();
}
