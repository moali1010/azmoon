package com.ebrahimi.azmoon.repository;

import com.ebrahimi.azmoon.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends MongoRepository<Question, String> {
    Page<Question> findAllByTeacherId(Integer teacherId, Pageable pageable);
}
