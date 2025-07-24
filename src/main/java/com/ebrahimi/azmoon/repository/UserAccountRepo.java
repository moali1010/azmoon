package com.ebrahimi.azmoon.repository;

import com.ebrahimi.azmoon.model.RegisterStatus;
import com.ebrahimi.azmoon.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, Integer>, JpaSpecificationExecutor<UserAccount> {
    boolean existsByUsername(String username);

    Page<UserAccount> findByRegisterStatus(RegisterStatus registerStatus, Pageable pageable);


    Optional<UserAccount> findByUsername(String username);
}
