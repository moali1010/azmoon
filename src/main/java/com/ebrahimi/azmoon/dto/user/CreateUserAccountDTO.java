package com.ebrahimi.azmoon.dto.user;

import com.ebrahimi.azmoon.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserAccountDTO {

    @NotBlank(message = "Name must NOT be Empty!")
    @NotNull(message = "Name must NOT be Null!")
    private String name;

    @NotBlank(message = "Username must NOT be Empty!")
    @NotNull(message = "Username must NOT be Null!")
    private String username;

    @NotBlank(message = "Password must NOT be Empty!")
    @NotNull(message = "Password must NOT be Null!")
    private String password;

    @NotNull(message = "Role must NOT be Null!")
    private UserRole userRole;
}
