package com.ebrahimi.azmoon.dto.exam;

import com.ebrahimi.azmoon.dto.course.CourseDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDTO {

    private Integer id;

    private CourseDTO course;

    private String title;

    private String description;

    private Integer duration;
}
