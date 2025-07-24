package com.ebrahimi.azmoon.dto.course;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageCourseDTO {
    private List<CourseDTO> data;
    private Long total;
}
