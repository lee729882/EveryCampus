package com.everycampus.controller;

import com.everycampus.entity.Survey;
import com.everycampus.entity.SurveyAnswer;
import com.everycampus.entity.SurveyQuestion;
import com.everycampus.repository.SurveyRepository;
import com.everycampus.repository.SurveyAnswerRepository;
import com.everycampus.service.SurveyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor

public class SurveyController {

    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository answerRepository;
    private final SurveyService surveyService;

    // ✅ 루트 URL 접속 시 설문 목록으로 이동
    @GetMapping("/")
    public String home() {
        return "redirect:/survey";
    }

    // 설문 목록
    @GetMapping("/survey")
    public String listSurveys(Model model) {
        List<Survey> surveys = surveyRepository.findAll();
        model.addAttribute("surveys", surveys);
        return "survey_list";
    }

    // 설문 생성 폼
    @GetMapping("/survey/create")
    public String createSurveyForm(Model model) {
        model.addAttribute("survey", new Survey());
        return "survey_create";
    }

    // 설문 생성 처리
    @PostMapping("/survey/create")
    public String createSurvey(HttpServletRequest request) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String[] questionTexts = request.getParameterValues("questionTexts");
        String[] types = request.getParameterValues("types");

        List<SurveyQuestion> questionList = new ArrayList<>();

        if (questionTexts != null && types != null) {
            for (int i = 0; i < questionTexts.length; i++) {
                String type = types[i];
                SurveyQuestion q = new SurveyQuestion();
                q.setQuestionText(questionTexts[i]);
                q.setType(type);

                if ("객관식".equals(type)) {
                    String[] choicesArr = request.getParameterValues("choices" + i);
                    if (choicesArr != null) {
                        q.setChoices(Arrays.asList(choicesArr));
                    }
                }
                questionList.add(q);
            }
        }

        Survey survey = new Survey();
        survey.setTitle(title);
        survey.setDescription(description);
        survey.setQuestions(questionList);

        for (SurveyQuestion q : questionList) q.setSurvey(survey);

        surveyRepository.save(survey);
        return "redirect:/survey";
    }

    // 설문 상세 보기 및 응답 폼
    @GetMapping("/survey/{id}")
    public String surveyDetail(@PathVariable Long id, Model model, HttpSession session) {
        Survey survey = surveyRepository.findById(id).orElseThrow();
        String username = (String) session.getAttribute("username");
        model.addAttribute("survey", survey);
        model.addAttribute("username", username);
        return "survey_detail";
    }

    // 설문 응답 제출
    @PostMapping("/survey/{id}/answer")
    public String submitAnswer(@PathVariable Long id, HttpServletRequest request) {
        Survey survey = surveyRepository.findById(id).orElseThrow();

        SurveyAnswer answer = new SurveyAnswer();
        answer.setSurvey(survey);
        answer.setUsername("익명");

        Map<Long, String> answersMap = new HashMap<>();
        for (SurveyQuestion q : survey.getQuestions()) {
            String value = request.getParameter("answers[" + q.getId() + "]");
            if (value != null) answersMap.put(q.getId(), value);
        }
        answer.setAnswers(answersMap);

        answerRepository.save(answer);
        return "redirect:/survey/result/" + id;
    }



    // 설문 삭제
    @PostMapping("/survey/delete/{id}")
    public String deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return "redirect:/survey";
    }
}


