package com.ebrahimi.azmoon.service;

import com.ebrahimi.azmoon.dto.PageRequestDTO;
import com.ebrahimi.azmoon.dto.user.*;
import com.ebrahimi.azmoon.exception.AlreadyExistException;
import com.ebrahimi.azmoon.exception.NotFoundException;
import com.ebrahimi.azmoon.model.RegisterStatus;
import com.ebrahimi.azmoon.model.UserAccount;
import com.ebrahimi.azmoon.repository.UserAccountRepo;
import com.ebrahimi.azmoon.repository.UserAccountSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepo userAccountRepo, PasswordEncoder passwordEncoder) {
        this.userAccountRepo = userAccountRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public PageUserDTO findAllWaitingUsers(PageRequestDTO pageRequestDTO) {
        Page<UserAccount> waitingUserPage = userAccountRepo
                .findByRegisterStatus(RegisterStatus.WAITING,
                        PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));
        List<UserAccountDTO> data = waitingUserPage.stream()
                .map(waitingUser -> UserAccountDTO.builder()
                        .id(waitingUser.getId())
                        .name(waitingUser.getName())
                        .username(waitingUser.getUsername())
                        .password(waitingUser.getPassword())
                        .userRole(waitingUser.getUserRole())
                        .registerStatus(waitingUser.getRegisterStatus())
                        .build())
                .toList();
        return PageUserDTO.builder().data(data).total(waitingUserPage.getTotalElements()).build();
    }

    @Transactional
    public Boolean changeUserInfo(PatchUserAccountDTO patchUserAccountDTO) {
        UserAccount userAccountFound = userAccountRepo
                .findById(patchUserAccountDTO.getId()).orElseThrow(()
                        -> new NotFoundException("UserAccount not found"));
        if (patchUserAccountDTO.getName() != null) {
            userAccountFound.setName(patchUserAccountDTO.getName());
        }
        if (patchUserAccountDTO.getUsername() != null) {
            userAccountFound.setUsername(patchUserAccountDTO.getUsername());
        }
        if (patchUserAccountDTO.getPassword() != null) {
            userAccountFound.setPassword(patchUserAccountDTO.getPassword());
        }
        if (patchUserAccountDTO.getUserRole() != null) {
            userAccountFound.setUserRole(patchUserAccountDTO.getUserRole());
        }
        if (patchUserAccountDTO.getRegisterStatus() != null) {
            userAccountFound.setRegisterStatus(patchUserAccountDTO.getRegisterStatus());
        }
        userAccountRepo.save(userAccountFound);
        return true;
    }

    public PageUserDTO getFilteredUsers(UserAccountFilterDTO filter, PageRequestDTO pageRequestDTO) {
        Page<UserAccount> users = userAccountRepo
                .findAll(UserAccountSpecification.filter(filter),
                        PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));
        List<UserAccountDTO> data = users.stream()
                .map(userAccount -> UserAccountDTO.builder()
                        .id(userAccount.getId())
                        .name(userAccount.getName())
                        .username(userAccount.getUsername())
                        .password(userAccount.getPassword())
                        .userRole(userAccount.getUserRole())
                        .registerStatus(userAccount.getRegisterStatus())
                        .build())
                .toList();
        return PageUserDTO.builder().data(data).total(users.getTotalElements()).build();
    }

    @Transactional
    public Boolean register(CreateUserAccountDTO createUserAccountDTO) {
        if (userAccountRepo.existsByUsername(createUserAccountDTO.getUsername())) {
            throw new AlreadyExistException("Username already exists");
        }
        UserAccount userAccount = UserAccount.builder()
                .name(createUserAccountDTO.getName())
                .username(createUserAccountDTO.getUsername())
                .password(passwordEncoder.encode(createUserAccountDTO.getPassword()))
                .userRole(createUserAccountDTO.getUserRole())
                .registerStatus(RegisterStatus.WAITING)
                .build();
        userAccountRepo.save(userAccount);
        return Boolean.TRUE;
    }
}
