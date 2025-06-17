package com.everycampus.controller;

import com.everycampus.entity.Survey;
import com.everycampus.entity.SurveyAnswer;
import com.everycampus.repository.SurveyRepository;
import com.everycampus.repository.SurveyAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/survey/answer")
public class SurveyAnswerController {

    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;

    // 설문 응답 폼
    @GetMapping("/{surveyId}")
    public String answerForm(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow();
        model.addAttribute("survey", survey);
        model.addAttribute("surveyAnswer", new SurveyAnswer());
        return "survey/answer";
    }

    // 설문 응답 저장
    @PostMapping("/{surveyId}")
    public String submitAnswer(@PathVariable Long surveyId,
                               @ModelAttribute SurveyAnswer surveyAnswer,
                               @SessionAttribute(value = "username", required = false) String username) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow();
        surveyAnswer.setSurvey(survey);
        surveyAnswer.setUsername(username != null ? username : "익명");
        surveyAnswerRepository.save(surveyAnswer);
        return "redirect:/survey/result/" + surveyId;
    }
   }
