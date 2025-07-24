package com.ebrahimi.azmoon.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchCourseDTO {
    @NotNull(message = "Course ID must NOT be Null!")
    private Integer courseID;
    private Integer teacherID;
    private List<Integer> studentIDs;
    private String courseName;
}
