package com.ebrahimi.azmoon.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "questions_document")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String title;
    private String content;
    private QuestionType questionType;
    private Integer score;

    private Integer teacherId;
}
