package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecture")
public class Lecture {
    @Id
    @Column(name = "lecture_code")
    private String lectureCode;

    private String lectureName;
    private String professorName;
    private String department;
}
