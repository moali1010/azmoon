package com.ebrahimi.azmoon.repository;

import com.ebrahimi.azmoon.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {
    boolean existsCourseByCourseName(String courseName);

    Page<Course> findByTeacherId(Integer id, Pageable pageable);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    Page<Course> findByStudentId(@Param("studentId") Integer studentId, Pageable pageable);

    Page<Course> findByStudents_Id(Integer studentId, Pageable pageable);
}

