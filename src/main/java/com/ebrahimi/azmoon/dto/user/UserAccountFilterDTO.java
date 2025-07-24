package com.ebrahimi.azmoon.dto.user;

import com.ebrahimi.azmoon.model.RegisterStatus;
import com.ebrahimi.azmoon.model.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountFilterDTO {

    private String name;

    private String username;

    private String password;

    private UserRole userRole;

    private RegisterStatus registerStatus;
}
