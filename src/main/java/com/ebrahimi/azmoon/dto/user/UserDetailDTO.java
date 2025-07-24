package com.ebrahimi.azmoon.dto.user;

import com.ebrahimi.azmoon.model.RegisterStatus;
import com.ebrahimi.azmoon.model.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {
    private Integer id;

    private String name;

    private UserRole userRole;

    private RegisterStatus registerStatus;
}
