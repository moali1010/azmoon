package com.ebrahimi.azmoon.service;

import com.ebrahimi.azmoon.domain.ExamQuestion;
import com.ebrahimi.azmoon.domain.Question;
import com.ebrahimi.azmoon.dto.question.ExamQuestionDTO;
import com.ebrahimi.azmoon.repository.ExamQuestionRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamQuestionService {
    private final ExamQuestionRepo examQuestionRepo;
    private final QuestionService questionService;

    public ExamQuestionService(ExamQuestionRepo examQuestionRepo, QuestionService questionService) {
        this.examQuestionRepo = examQuestionRepo;
        this.questionService = questionService;
    }

    public Boolean addExamQuestion(ExamQuestionDTO examQuestionDTO) {
        ExamQuestion examQuestion = examQuestionRepo
                .findByExamId(examQuestionDTO.getExamId()).orElse(ExamQuestion.builder().build());
        examQuestion.setExamId(examQuestionDTO.getExamId());
        List<Question> questionList = examQuestionDTO.getQuestions().stream()
                .map(questionDTO -> {
                    if (questionDTO.getID() == null) return questionService.addQuestion(questionDTO);
                    else return questionService.updateQuestion(questionDTO);
                }).collect(Collectors.toList());
        examQuestion.setQuestions(questionList);
        examQuestionRepo.save(examQuestion);
        return true;
    }
}
