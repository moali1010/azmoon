package com.ebrahimi.azmoon.dto.course;

import com.ebrahimi.azmoon.dto.user.UserDetailDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailDTO {
    private Integer courseId;

    private String courseName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private UserDetailDTO teacher;

    private List<UserDetailDTO> students;
}
