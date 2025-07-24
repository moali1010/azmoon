package com.ebrahimi.azmoon.repository;

import com.ebrahimi.azmoon.domain.ExamQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamQuestionRepo extends MongoRepository<ExamQuestion, String> {
    Optional<ExamQuestion> findByExamId(Integer id);
}
