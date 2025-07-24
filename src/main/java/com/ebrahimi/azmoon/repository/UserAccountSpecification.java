package com.ebrahimi.azmoon.repository;

import com.ebrahimi.azmoon.dto.user.UserAccountFilterDTO;
import com.ebrahimi.azmoon.model.UserAccount;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserAccountSpecification {

    public static Specification<UserAccount> filter(UserAccountFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null)
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));

            if (filter.getUserRole() != null)
                predicates.add(cb.equal(root.get("userRole"), filter.getUserRole()));

            if (filter.getRegisterStatus() != null)
                predicates.add(cb.equal(root.get("registerStatus"), filter.getRegisterStatus()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}