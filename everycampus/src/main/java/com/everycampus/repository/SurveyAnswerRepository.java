package com.everycampus.repository;

import com.everycampus.entity.Survey;
import com.everycampus.entity.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {
    List<SurveyAnswer> findBySurveyId(Long surveyId);
    List<SurveyAnswer> findByUsername(String username);
    List<SurveyAnswer> findBySurvey(Survey survey);
    void deleteBySurvey(Survey survey);
}
