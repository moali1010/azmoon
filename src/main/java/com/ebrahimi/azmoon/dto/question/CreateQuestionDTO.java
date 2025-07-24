package com.ebrahimi.azmoon.dto.question;

import com.ebrahimi.azmoon.domain.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionDTO {

    @NotBlank(message = "TITLE must NOT be Empty!")
    @NotNull(message = "TITLE must NOT be Null!")
    private String title;

    @NotBlank(message = "CONTENT must NOT be Empty!")
    @NotNull(message = "CONTENT must NOT be Null!")
    private String content;

    @NotNull(message = "QUESTION-TYPE must NOT be Null!")
    private QuestionType questionType;

    @NotNull(message = "SCORE must NOT be Null!")
    private Integer score;

    @NotNull(message = "TEACHER-ID must NOT be Null!")
    private Integer teacherId;

    private Map<Integer, String> choices;
    private Integer answer;

    private String ID;

}
