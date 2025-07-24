package com.ebrahimi.azmoon.repository;

import com.ebrahimi.azmoon.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepo extends JpaRepository<Exam, Integer> {
    List<Exam> findByCourseId(Integer courseId);

    boolean existsExamByTitle(String title);

    @Query("select count(e) > 0 from Exam e join e.course c join c.students s where s.id=:studentId and e.id=:examId ")
    boolean studentHasAccessToExam(@Param("studentId") Integer studentId, @Param("examId") Integer examId);
}
