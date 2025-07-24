package com.ebrahimi.azmoon.controller;

import com.ebrahimi.azmoon.dto.user.CreateUserAccountDTO;
import com.ebrahimi.azmoon.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserAccountController {
    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> createUserAccount(@RequestBody @Validated CreateUserAccountDTO createUserAccountDTO) {
        Boolean result = userAccountService.register(createUserAccountDTO);
        return ResponseEntity.ok(result);
    }
}
