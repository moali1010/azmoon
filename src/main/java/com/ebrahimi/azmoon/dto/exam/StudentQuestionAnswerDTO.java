package com.ebrahimi.azmoon.dto.exam;

import com.ebrahimi.azmoon.domain.StudentAnswer;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuestionAnswerDTO {
    private Integer examId;
    private List<StudentAnswer> studentAnswers;
}
