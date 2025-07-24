package com.ebrahimi.azmoon.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswer {
    private String questionId;
    private String answer;
    private Integer choice;

    private Integer studentScore;
}
