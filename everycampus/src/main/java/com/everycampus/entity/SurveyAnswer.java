package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // 누가 응답했는지

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ElementCollection
    @CollectionTable(name = "survey_answer_map", joinColumns = @JoinColumn(name = "answer_id"))
    @MapKeyColumn(name = "question_id") // 문항 ID를 키로
    @Column(name = "answer_value")      // 응답 내용을 값으로
    private Map<Long, String> answers = new HashMap<>();
}
