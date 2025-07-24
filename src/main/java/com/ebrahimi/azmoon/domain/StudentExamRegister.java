package com.ebrahimi.azmoon.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "exam_register_document")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentExamRegister {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private Integer studentId;
    private Integer examId;
    private LocalDateTime examStart;

    private List<StudentAnswer> studentAnswers;
    @Builder.Default
    private Integer grade = 0;
}
