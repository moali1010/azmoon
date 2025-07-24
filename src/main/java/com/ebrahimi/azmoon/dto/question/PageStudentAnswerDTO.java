package com.ebrahimi.azmoon.dto.question;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageStudentAnswerDTO {
    private List<StudentExamQuestionRecordedDTO> data;
    private Long total;
}
