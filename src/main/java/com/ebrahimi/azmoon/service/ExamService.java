package com.ebrahimi.azmoon.service;

import com.ebrahimi.azmoon.domain.*;
import com.ebrahimi.azmoon.dto.PageRequestDTO;
import com.ebrahimi.azmoon.dto.course.CourseDTO;
import com.ebrahimi.azmoon.dto.exam.*;
import com.ebrahimi.azmoon.dto.question.CreateQuestionDTO;
import com.ebrahimi.azmoon.dto.question.PageStudentAnswerDTO;
import com.ebrahimi.azmoon.dto.question.StudentExamQuestionRecordedDTO;
import com.ebrahimi.azmoon.exception.AlreadyExistException;
import com.ebrahimi.azmoon.exception.BadRequestException;
import com.ebrahimi.azmoon.exception.NotFoundException;
import com.ebrahimi.azmoon.model.Course;
import com.ebrahimi.azmoon.model.Exam;
import com.ebrahimi.azmoon.repository.*;
import com.ebrahimi.azmoon.security.util.SecurityHelper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamRepo examRepo;
    private final CourseRepo courseRepo;
    private final UserAccountRepo userAccountRepo;
    private final StudentExamRegisterRepo studentExamRegisterRepo;
    private final ExamQuestionRepo examQuestionRepo;

    public ExamService(ExamRepo examRepo, CourseRepo courseRepo, UserAccountRepo userAccountRepo,
                       StudentExamRegisterRepo studentExamRegisterRepo, ExamQuestionRepo examQuestionRepo) {
        this.examRepo = examRepo;
        this.courseRepo = courseRepo;
        this.userAccountRepo = userAccountRepo;
        this.studentExamRegisterRepo = studentExamRegisterRepo;
        this.examQuestionRepo = examQuestionRepo;
    }

    public List<ExamDTO> findByCourseId(Integer courseId) {
        if (!courseRepo.existsById(courseId)) {
            throw new NotFoundException("Course NOT found!");
        }
        List<Exam> exams = examRepo.findByCourseId(courseId);
        List<ExamDTO> list = exams.stream().map(exam ->
                ExamDTO.builder()
                        .id(exam.getId())
                        .course(CourseDTO.builder()
                                .id(exam.getCourse().getId())
                                .courseName(exam.getCourse().getCourseName())
                                .startDate(exam.getCourse().getStartDate())
                                .endDate(exam.getCourse().getEndDate())
                                .build())
                        .title(exam.getTitle())
                        .description(exam.getDescription())
                        .duration(exam.getDuration())
                        .build()
        ).collect(Collectors.toList());
        return list;
    }

    @Transactional
    public Boolean addExam(CreateExamDTO exam) {
        if (examRepo.existsExamByTitle(exam.getTitle())) {
            throw new AlreadyExistException("Exam already exists");
        }
        Course course = courseRepo.findById(exam.getCourseId()).orElseThrow(()
                -> new NotFoundException("Course NOT found!"));
        Exam newExam = Exam.builder()
                .title(exam.getTitle())
                .description(exam.getDescription())
                .duration(exam.getDuration())
                .course(course)
                .build();
        examRepo.save(newExam);
        return true;
    }

    @Transactional
    public Boolean updateExam(PatchExamDTO patchExam) {
        Exam exam = examRepo.findById(patchExam.getId()).orElseThrow(()
                -> new NotFoundException("Exam not found!"));
        if (patchExam.getTitle() != null) {
            exam.setTitle(patchExam.getTitle());
        }
        if (patchExam.getDescription() != null) {
            exam.setDescription(patchExam.getDescription());
        }
        if (patchExam.getDuration() != null) {
            exam.setDuration(patchExam.getDuration());
        }
        if (patchExam.getCourseId() != null) {
            Course course = courseRepo.findById(patchExam.getCourseId()).orElseThrow(()
                    -> new NotFoundException("Course not found!"));
            exam.setCourse(course);
        }
        examRepo.save(exam);
        return true;
    }

    @Transactional
    public Boolean deleteExam(Integer id) {
        if (!examRepo.existsById(id)) {
            throw new NotFoundException("Exam NOT found!");
        }
        examRepo.deleteById(id);
        return true;
    }

    public Boolean registerExam(Integer examId) {
        Integer studentId = SecurityHelper.getCurrentUser().getId();
//        UserAccount maybeStudent = userAccountRepo.findById(studentId)
//                .orElseThrow(() -> new NotFoundException("User Not Found!"));
//        if (!maybeStudent.getUserRole().equals(UserRole.STUDENT)) {
//            throw new BadRequestException("This Student is Not Student!");
//        }
//        if (maybeStudent.getRegisterStatus().equals(RegisterStatus.WAITING)) {
//            throw new BadRequestException("This Student is Not APPROVED!");
//        }
        if (!examRepo.studentHasAccessToExam(studentId, examId)) {
            throw new BadRequestException("Don't have access to this exam!");
        }
        if (studentExamRegisterRepo.existsByStudentIdAndExamId(studentId, examId)) {
            throw new AlreadyExistException("Register already exists!");
        }
        StudentExamRegister studentExamRegister = StudentExamRegister.builder()
                .studentId(studentId)
                .examId(examId)
                .examStart(LocalDateTime.now()
                ).build();
        studentExamRegisterRepo.save(studentExamRegister);
        return true;
    }

    public Boolean saveAnswerExam(StudentQuestionAnswerDTO answerDTO) {
        Integer studentId = SecurityHelper.getCurrentUser().getId();
        Exam exam = examRepo.findById(answerDTO.getExamId()).orElseThrow(()
                -> new NotFoundException("Exam Not Found!"));
        StudentExamRegister studentExam = studentExamRegisterRepo
                .findByStudentIdAndExamId(studentId, exam.getId())
                .orElseThrow(() -> new BadRequestException("You must First Register!"));
        if (LocalDateTime.now().isAfter(studentExam.getExamStart().plusMinutes(exam.getDuration())))
            throw new BadRequestException("Exam is FINISHED!");
        studentExam.setStudentAnswers(answerDTO.getStudentAnswers());
        studentExamRegisterRepo.save(studentExam);
        return true;
    }

    public Boolean calculateExamScore(Integer examId) {
        ExamQuestion exam = examQuestionRepo.findByExamId(examId).orElseThrow(()
                -> new NotFoundException("Exam Not Found!"));
        List<StudentExamRegister> studentRegisterList = studentExamRegisterRepo.findAllByExamId(examId);
        for (StudentExamRegister studentRegister : studentRegisterList) {
            studentRegister.getStudentAnswers().stream()
                    .filter(sa -> sa.getChoice() != null)
                    .forEach(sa -> {
                        exam.getQuestions().stream().filter(q -> q.getId().equals(sa.getQuestionId()))
                                .forEach(q -> {
                                    if (Objects.equals(((MultipleChoiceQuestion) q).getAnswer(), sa.getChoice())) {
                                        sa.setStudentScore(q.getScore());
                                    } else
                                        sa.setStudentScore(0);
                                });
                    });
            Integer grade = calcGrade(studentRegister);
            studentRegister.setGrade(grade);
        }
        studentExamRegisterRepo.saveAll(studentRegisterList);
        return true;
    }

    public PageStudentAnswerDTO findAllStudentsAnswerByExamId(Integer examId, PageRequestDTO pageRequestDTO) {
        Page<StudentExamRegister> allRegisters = studentExamRegisterRepo
                .findAllByExamId(examId, PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));
        ExamQuestion examQuestion = examQuestionRepo
                .findByExamId(examId).orElseThrow(() -> new NotFoundException("Exam Not Found!"));
        List<StudentExamQuestionRecordedDTO> data = allRegisters.stream()
                .map(studentExamRegister ->
                        StudentExamQuestionRecordedDTO.builder()
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
                                                        questionDTO.setChoices(((MultipleChoiceQuestion) question).getChoices());
                                                    }
                                                    return questionDTO;
                                                }
                                        ).toList())
                                .studentAnswers(studentExamRegister.getStudentAnswers())
                                .build()
                ).collect(Collectors.toList());
        return PageStudentAnswerDTO.builder().data(data).total(allRegisters.getTotalElements()).build();
    }

    public Boolean setScore(PatchScoreDTO patchScoreDTO) {
        StudentExamRegister studentRegister = studentExamRegisterRepo.findById(patchScoreDTO
                .getStudentRegisterId()).orElseThrow(() -> new NotFoundException("Student Register Not Found!"));
        ExamQuestion exam = examQuestionRepo.findByExamId(studentRegister.getExamId())
                .orElseThrow(() -> new NotFoundException("Exam Not Found!"));
        Integer scoreQuestion = exam.getQuestions().stream()
                .filter(q -> q.getId().equals(patchScoreDTO.getQuestionId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Question Not Found!")).getScore();
        if (patchScoreDTO.getScore() > scoreQuestion) {
            throw new NotFoundException("Score Not Greater than " + scoreQuestion);
        }
        studentRegister.getStudentAnswers()
                .stream()
                .filter(studentAnswer -> studentAnswer.getQuestionId()
                        .equals(patchScoreDTO.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Student answer for this question Not Found!"))
                .setStudentScore(patchScoreDTO.getScore());
        Integer grade = calcGrade(studentRegister);
        studentRegister.setGrade(grade);
        studentExamRegisterRepo.save(studentRegister);
        return true;
    }

    private Integer calcGrade(StudentExamRegister studentExamRegister) {
        int grade = 0;
        for (StudentAnswer studentAnswer : studentExamRegister.getStudentAnswers()) {
            if (studentAnswer.getStudentScore() != null) {
                grade += studentAnswer.getStudentScore();
            }
        }
        return grade;
    }
}
