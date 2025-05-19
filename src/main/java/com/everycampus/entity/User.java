package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"users\"")  // 또는 다른 예약어가 아닌 이름 사용
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String school;
    
    @Column(nullable = false, unique = true)
    private String email;
}