package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;
    private String type; // "객관식", "주관식"

    @ElementCollection
    private List<String> choices;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;
}
