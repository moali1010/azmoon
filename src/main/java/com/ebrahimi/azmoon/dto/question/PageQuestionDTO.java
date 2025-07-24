package com.ebrahimi.azmoon.dto.question;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageQuestionDTO {
    private List<QuestionDTO> data;
    private Long total;
}
