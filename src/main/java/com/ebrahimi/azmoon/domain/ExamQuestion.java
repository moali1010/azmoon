package com.ebrahimi.azmoon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "exam_question_document")
@Builder
@Getter
@Setter
public class ExamQuestion {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private Integer examId;
    private List<Question> questions;
}
