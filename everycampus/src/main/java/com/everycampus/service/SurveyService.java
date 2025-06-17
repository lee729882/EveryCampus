package com.everycampus.service;

import com.everycampus.entity.Survey;
import com.everycampus.repository.SurveyRepository;
import com.everycampus.repository.SurveyAnswerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;

    @Transactional
    public void deleteSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow();

        // 1. 설문 응답 먼저 삭제
        surveyAnswerRepository.deleteBySurvey(survey);

        // 2. 설문 문항(questions)은 orphanRemoval 또는 cascade 설정이 있다면 자동 제거됨
        //    그렇지 않다면 아래처럼 수동으로 제거 필요
        survey.getQuestions().clear(); // 연관관계 제거
        surveyRepository.save(survey); // 반영 저장

        // 3. 설문 삭제
        surveyRepository.delete(survey);
    }
}
