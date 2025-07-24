package com.ebrahimi.azmoon.dto.course;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Integer id;

    private String courseName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
