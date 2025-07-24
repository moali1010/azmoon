package com.ebrahimi.azmoon.controller;

import com.ebrahimi.azmoon.dto.PageRequestDTO;
import com.ebrahimi.azmoon.dto.course.PageCourseDTO;
import com.ebrahimi.azmoon.dto.exam.CreateExamDTO;
import com.ebrahimi.azmoon.dto.exam.ExamDTO;
import com.ebrahimi.azmoon.dto.exam.PatchExamDTO;
import com.ebrahimi.azmoon.dto.exam.PatchScoreDTO;
import com.ebrahimi.azmoon.dto.question.ExamQuestionDTO;
import com.ebrahimi.azmoon.dto.question.PageQuestionDTO;
import com.ebrahimi.azmoon.dto.question.PageStudentAnswerDTO;
import com.ebrahimi.azmoon.service.CourseService;
import com.ebrahimi.azmoon.service.ExamQuestionService;
import com.ebrahimi.azmoon.service.ExamService;
import com.ebrahimi.azmoon.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")

@PreAuthorize("hasAnyRole('TEACHER')")
public class TeacherController {
    private final CourseService courseService;
    private final ExamService examService;
    private final ExamQuestionService examQuestionService;
    private final QuestionService questionService;

    public TeacherController(CourseService courseService, ExamService examService, ExamQuestionService examQuestionService, QuestionService questionService) {
        this.courseService = courseService;
        this.examService = examService;
        this.examQuestionService = examQuestionService;
        this.questionService = questionService;
    }

    @GetMapping("/my-courses")
    public ResponseEntity<PageCourseDTO> getCourseDetail(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(courseService.findAllTeacherCourses(PageRequestDTO.builder().page(page).size(size).build()));
    }

    @GetMapping("/course-exams")
    public ResponseEntity<List<ExamDTO>> getCourseDetail(@RequestParam("courseId") Integer id) {
        return ResponseEntity.ok(examService.findByCourseId(id));
    }

    @PostMapping("/add-exam")
    public ResponseEntity<Boolean> addExam(@RequestBody @Validated CreateExamDTO createExamDTO) {
        Boolean result = examService.addExam(createExamDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update-exam")
    public ResponseEntity<Boolean> pathExam(@RequestBody @Validated PatchExamDTO patchExamDTO) {
        Boolean result = examService.updateExam(patchExamDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete-exam")
    public ResponseEntity<Boolean> deleteExam(@RequestParam("examId") Integer examId) {
        Boolean result = examService.deleteExam(examId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/questions")
    public ResponseEntity<PageQuestionDTO> findAllQuestions(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(questionService.findAllQuestionByTeacher(PageRequestDTO.builder().page(page).size(size).build()));
    }

    @PatchMapping("/exam-question")
    public ResponseEntity<Boolean> addExamQuestion(@RequestBody @Validated ExamQuestionDTO examQuestionDTO) {
        Boolean result = examQuestionService.addExamQuestion(examQuestionDTO);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/questions-exam")
    public ResponseEntity<ExamQuestionDTO> findQuestionsByExamId(@RequestParam(name = "examId") Integer examId) {
        return ResponseEntity.ok(questionService.findQuestionByExamId(examId));
    }

    @PostMapping("/calc-score")
    public ResponseEntity<Boolean> calculateScore(@RequestParam(name = "examId") Integer examId) {
        return ResponseEntity.ok(examService.calculateExamScore(examId));
    }

    @GetMapping("/exam-students")
    public ResponseEntity<PageStudentAnswerDTO> studentsInExam(@RequestParam(name = "examId") Integer examId,
                                                               @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                               @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(examService.findAllStudentsAnswerByExamId(examId, PageRequestDTO.builder().page(page).size(size).build()));
    }

    @PatchMapping("/set-score")
    public ResponseEntity<Boolean> setScore(@RequestBody @Validated PatchScoreDTO patchScoreDTO) {
        return ResponseEntity.ok(examService.setScore(patchScoreDTO));
    }
}
