package com.ebrahimi.azmoon.repository;

import com.ebrahimi.azmoon.domain.StudentExamRegister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamRegisterRepo extends MongoRepository<StudentExamRegister, String> {
    boolean existsByStudentIdAndExamId(Integer studentId, Integer examId);

    Optional<StudentExamRegister> findByStudentIdAndExamId(Integer studentId, Integer examId);

    List<StudentExamRegister> findAllByExamId(Integer examId);

    Page<StudentExamRegister> findAllByExamId(Integer examId, Pageable pageable);
}
