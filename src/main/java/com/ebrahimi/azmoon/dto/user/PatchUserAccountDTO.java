package com.ebrahimi.azmoon.dto.user;

import com.ebrahimi.azmoon.model.RegisterStatus;
import com.ebrahimi.azmoon.model.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserAccountDTO {
    @NotNull(message = "ID must NOT be Null!")
    private Integer id;

    private String name;

    private String username;

    private String password;

    private UserRole userRole;

    private RegisterStatus registerStatus;
}
