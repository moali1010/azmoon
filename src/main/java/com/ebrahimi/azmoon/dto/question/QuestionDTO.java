package com.ebrahimi.azmoon.dto.question;

import com.ebrahimi.azmoon.domain.QuestionType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private String id;
    private String title;
    private String content;
    private QuestionType questionType;
    private Integer score;
    private Integer teacherId;
    private Map<Integer, String> choices;
    private Integer answer;
}
