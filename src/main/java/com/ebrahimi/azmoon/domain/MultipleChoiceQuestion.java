package com.ebrahimi.azmoon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceQuestion extends Question {
    private Map<Integer, String> choices;
    private Integer answer;
}
