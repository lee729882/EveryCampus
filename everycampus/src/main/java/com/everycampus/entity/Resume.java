package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;

    private String school1;
    private String major1;
    private String school2;
    private String major2;

    private String company1;
    private String role1;
    private String company2;
    private String role2;

    private String language;
    private String certificate;

    private String username;  // 로그인 사용자명
}
