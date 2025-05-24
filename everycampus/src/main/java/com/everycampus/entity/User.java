package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_old")  // 새 테이블 이름으로 변경
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