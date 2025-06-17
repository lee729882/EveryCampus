package com.everycampus.controller;

import com.everycampus.entity.Survey;
import com.everycampus.entity.SurveyAnswer;
import com.everycampus.entity.SurveyQuestion;
import com.everycampus.repository.SurveyRepository;
import com.everycampus.repository.SurveyAnswerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/survey/result")
public class SurveyResultController {

    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;

    @GetMapping("/{surveyId}")
    public String surveyResult(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow();
        List<SurveyAnswer> answers = surveyAnswerRepository.findBySurveyId(surveyId);

        // 통계 데이터 계산
        Map<Long, List<Map<String, Object>>> stats = new HashMap<>();

        for (SurveyQuestion question : survey.getQuestions()) {
            if ("객관식".equals(question.getType())) {
                Map<String, Integer> countMap = new LinkedHashMap<>();
                for (String choice : question.getChoices()) {
                    countMap.put(choice, 0);
                }
                for (SurveyAnswer answer : answers) {
                    String selected = answer.getAnswers().get(question.getId());
                    if (selected != null && countMap.containsKey(selected)) {
                        countMap.put(selected, countMap.get(selected) + 1);
                    }
                }

                int total = countMap.values().stream().mapToInt(Integer::intValue).sum();

                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("choice", entry.getKey());
                    result.put("count", entry.getValue());
                    result.put("percent", total == 0 ? 0 : (entry.getValue() * 100 / total));
                    resultList.add(result);
                }

                stats.put(question.getId(), resultList);
            } else if ("주관식".equals(question.getType())) {
                List<String> subjectiveAnswers = new ArrayList<>();
                for (SurveyAnswer answer : answers) {
                    String text = answer.getAnswers().get(question.getId());
                    if (text != null && !text.isBlank()) {
                        subjectiveAnswers.add(text);
                    }
                }

                List<Map<String, Object>> resultList = new ArrayList<>();
                for (String ans : subjectiveAnswers) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("text", ans);
                    resultList.add(entry);
                }

                stats.put(question.getId(), resultList);
            }
        }

        model.addAttribute("survey", survey);
        model.addAttribute("answers", answers);
        model.addAttribute("stats", stats);
        return "survey_result";
    }
}
