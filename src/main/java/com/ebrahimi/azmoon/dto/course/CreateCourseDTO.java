package com.ebrahimi.azmoon.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseDTO {
    @NotNull(message = "Course-Name must NOT be Null!")
    private String courseName;

    @NotNull(message = "Start-Date must NOT be Null!")
    private LocalDateTime startDate;

    @NotNull(message = "End-Date must NOT be Null!")
    private LocalDateTime endDate;

}
