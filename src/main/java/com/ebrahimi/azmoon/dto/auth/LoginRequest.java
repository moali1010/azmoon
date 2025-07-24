package com.ebrahimi.azmoon.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginRequest {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
