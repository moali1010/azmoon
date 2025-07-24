package com.ebrahimi.azmoon.controller;

import com.ebrahimi.azmoon.dto.PageRequestDTO;
import com.ebrahimi.azmoon.dto.course.PageCourseDTO;
import com.ebrahimi.azmoon.dto.exam.ExamDTO;
import com.ebrahimi.azmoon.dto.exam.StudentQuestionAnswerDTO;
import com.ebrahimi.azmoon.dto.question.StudentExamQuestionRecordedDTO;
import com.ebrahimi.azmoon.service.CourseService;
import com.ebrahimi.azmoon.service.ExamService;
import com.ebrahimi.azmoon.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@PreAuthorize("hasAnyRole('STUDENT')")
public class StudentController {
    private final CourseService courseService;
    private final ExamService examService;
    private final QuestionService questionService;


    public StudentController(CourseService courseService, ExamService examService, QuestionService questionService) {
        this.courseService = courseService;
        this.examService = examService;
        this.questionService = questionService;
    }

    @GetMapping("/my-course")
    public ResponseEntity<PageCourseDTO> findAllCourse(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(courseService.findAllStudentCourses(PageRequestDTO.builder().page(page).size(size).build()));
    }

    @GetMapping("/course-exams")
    public ResponseEntity<List<ExamDTO>> getCourseExam(@RequestParam("courseId") Integer courseId) {
        return ResponseEntity.ok(examService.findByCourseId(courseId));
    }

    @PostMapping("/exam-register")
    public ResponseEntity<Boolean> examRegister(@RequestParam(name = "examId") Integer examId) {
        return ResponseEntity.ok(examService.registerExam(examId));
    }

    @GetMapping("/questions-exam")
    public ResponseEntity<StudentExamQuestionRecordedDTO> findQuestionsByExamId(@RequestParam(name = "examId") Integer examId) {
        return ResponseEntity.ok(questionService.findQuestionByExamIdForStudent(examId));
    }

    @PatchMapping("/set-answer")
    public ResponseEntity<Boolean> saveAnswerExam(@RequestBody StudentQuestionAnswerDTO answerDTO) {
        Boolean res = examService.saveAnswerExam(answerDTO);
        return ResponseEntity.ok(res);
    }
}
