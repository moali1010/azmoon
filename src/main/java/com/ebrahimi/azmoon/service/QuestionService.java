package com.ebrahimi.azmoon.service;

import com.ebrahimi.azmoon.domain.*;
import com.ebrahimi.azmoon.dto.PageRequestDTO;
import com.ebrahimi.azmoon.dto.question.*;
import com.ebrahimi.azmoon.exception.BadRequestException;
import com.ebrahimi.azmoon.exception.NotFoundException;
import com.ebrahimi.azmoon.repository.ExamQuestionRepo;
import com.ebrahimi.azmoon.repository.QuestionRepo;
import com.ebrahimi.azmoon.repository.StudentExamRegisterRepo;
import com.ebrahimi.azmoon.repository.UserAccountRepo;
import com.ebrahimi.azmoon.security.util.SecurityHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepo questionRepo;
    private final UserAccountRepo userAccountRepo;
    private final ExamQuestionRepo examQuestionRepo;
    private final StudentExamRegisterRepo studentExamRegisterRepo;

    public QuestionService(QuestionRepo questionRepo,
                           UserAccountRepo userAccountRepo, ExamQuestionRepo examQuestionRepo,
                           StudentExamRegisterRepo studentExamRegisterRepo) {
        this.questionRepo = questionRepo;
        this.userAccountRepo = userAccountRepo;
        this.examQuestionRepo = examQuestionRepo;
        this.studentExamRegisterRepo = studentExamRegisterRepo;
    }

    public Question updateQuestion(CreateQuestionDTO createQuestionDTO) {
        Question question = questionRepo.findById(createQuestionDTO.getID()).orElseThrow(()
                -> new NotFoundException("Question NOT Found!"));
        question.setQuestionType(createQuestionDTO.getQuestionType());
        question.setTitle(createQuestionDTO.getTitle());
        question.setContent(createQuestionDTO.getContent());
        question.setScore(createQuestionDTO.getScore());
        question.setTeacherId(SecurityHelper.getCurrentUser().getId());
        if (createQuestionDTO.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            ((MultipleChoiceQuestion) question).setChoices(createQuestionDTO.getChoices());
            ((MultipleChoiceQuestion) question).setAnswer(createQuestionDTO.getAnswer());
        }
        return questionRepo.save(question);

    }

    public Question addQuestion(CreateQuestionDTO createQuestionDTO) {
        Question question;
        if (createQuestionDTO.getQuestionType() == QuestionType.ESSAY) {
            question = EssayQuestion.builder()
                    .title(createQuestionDTO.getTitle())
                    .content(createQuestionDTO.getContent())
                    .questionType(createQuestionDTO.getQuestionType())
                    .score(createQuestionDTO.getScore())
                    .teacherId(SecurityHelper.getCurrentUser().getId())
                    .build();

        } else {
            question = MultipleChoiceQuestion.builder()
                    .title(createQuestionDTO.getTitle())
                    .content(createQuestionDTO.getContent())
                    .questionType(createQuestionDTO.getQuestionType())
                    .score(createQuestionDTO.getScore())
                    .teacherId(SecurityHelper.getCurrentUser().getId())
                    .choices(createQuestionDTO.getChoices())
                    .answer(createQuestionDTO.getAnswer())
                    .build();
        }
        return questionRepo.save(question);
    }

    public PageQuestionDTO findAllQuestionByTeacher(PageRequestDTO pageRequestDTO) {
        Integer teacherId = SecurityHelper.getCurrentUser().getId();
//        UserAccount maybeTeacher = userAccountRepo.findById(teacherId).orElseThrow(()
//                -> new NotFoundException("User Not Found!"));
//        if (!maybeTeacher.getUserRole().equals(UserRole.TEACHER)) {
//            throw new BadRequestException("User is not Teacher");
//        }
//        if (maybeTeacher.getRegisterStatus().equals(RegisterStatus.WAITING)) {
//            throw new BadRequestException("Teacher is Not APPROVED");
//        }
        Page<Question> questionList = questionRepo.findAllByTeacherId(teacherId,
                PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));
        List<QuestionDTO> data = questionList.stream()
                .map(question -> {
                    QuestionDTO questionDTO = QuestionDTO.builder()
                            .id(question.getId())
                            .title(question.getTitle())
                            .questionType(question.getQuestionType())
                            .score(question.getScore())
                            .teacherId(question.getTeacherId())
                            .content(question.getContent())
                            .build();
                    if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                        questionDTO.setAnswer(((MultipleChoiceQuestion) question).getAnswer());
                        questionDTO.setChoices(((MultipleChoiceQuestion) question).getChoices());
                    }
                    return questionDTO;
                }).toList();
        return PageQuestionDTO.builder().data(data).total(questionList.getTotalElements()).build();
    }

    public ExamQuestionDTO findQuestionByExamId(Integer examId) {
        ExamQuestion examQuestion = examQuestionRepo.findByExamId(examId).orElseThrow(()
                -> new NotFoundException("Exam Not Found!"));
        return ExamQuestionDTO.builder().examId(examId).questions(examQuestion.getQuestions().stream()
                .map(question -> {
                            CreateQuestionDTO questionDTO = CreateQuestionDTO.builder()
                                    .title(question.getTitle())
                                    .content(question.getContent())
                                    .questionType(question.getQuestionType())
                                    .score(question.getScore())
                                    .teacherId(question.getTeacherId())
                                    .ID(question.getId())
                                    .build();
                            if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                                questionDTO.setAnswer(((MultipleChoiceQuestion) question).getAnswer());
                                questionDTO.setChoices(((MultipleChoiceQuestion) question).getChoices());
                            }
                            return questionDTO;
                        }
                ).toList()).build();
    }

    public StudentExamQuestionRecordedDTO findQuestionByExamIdForStudent(Integer examId) {
        Integer studentId = SecurityHelper.getCurrentUser().getId();
        StudentExamRegister studentExam = studentExamRegisterRepo
                .findByStudentIdAndExamId(studentId, examId).orElseThrow(()
                        -> new BadRequestException("You must Register first!"));
        ExamQuestion examQuestion = examQuestionRepo
                .findByExamId(examId).orElseThrow(() -> new NotFoundException("Exam Not Found!"));
        return StudentExamQuestionRecordedDTO.builder()
                .examId(examId)
                .questions(examQuestion.getQuestions().stream()
                        .map(question -> {
                                    CreateQuestionDTO questionDTO = CreateQuestionDTO.builder()
                                            .title(question.getTitle())
                                            .content(question.getContent())
                                            .questionType(question.getQuestionType())
                                            .score(question.getScore())
                                            .teacherId(question.getTeacherId())
                                            .ID(question.getId())
                                            .build();
                                    if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
                                        questionDTO.setChoices(((MultipleChoiceQuestion) question)
                                                .getChoices());
                                    }
                                    return questionDTO;
                                }
                        ).toList())
                .studentAnswers(studentExam.getStudentAnswers())
                .build();
    }
}
