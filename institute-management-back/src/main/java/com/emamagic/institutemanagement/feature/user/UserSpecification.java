package com.emamagic.institutemanagement.feature.user;

import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.entity.UserRole;
import com.emamagic.institutemanagement.feature.user.dto.UserSearch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserSpecification {

    public static Specification<UserApp> findByCriteria(UserSearch req, Long currentUerId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            String name = req.name();
            String email = req.email();
            if ((name != null && !name.isBlank()) || (email != null && !email.isBlank())) {
                List<Predicate> orPredicates = new ArrayList<>();
                if (name != null && !name.isBlank()) {
                    orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%" + name.toLowerCase() + "%"));
                }
                if (email != null && !email.isBlank()) {
                    orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email + "%"));
                }
                predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
            }

            String gender = req.gender();
            if (gender != null && !gender.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), gender.toUpperCase()));
            }

            String age = req.age();
            if (age != null && !age.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("age"), age));
            }

            String role = req.role();
            if (role != null && !role.isBlank()) {
                Join<UserApp, UserRole> roleJoin = root.join("role");
                predicates.add(criteriaBuilder.equal(roleJoin.get("type"), role.toUpperCase()));
            }

            Boolean isVerified = req.isVerified();
            if (isVerified != null) {
                predicates.add(criteriaBuilder.equal(root.get("isVerified"), isVerified));
            }

            Boolean isApproved = req.isApproved();
            if (isApproved != null) {
                predicates.add(criteriaBuilder.equal(root.get("isApproved"), isApproved));
            }

            Boolean isProfileCompleted = req.isProfileCompleted();
            if (isProfileCompleted != null) {
                predicates.add(criteriaBuilder.equal(root.get("isProfileCompleted"), isProfileCompleted));
            }

            predicates.add(criteriaBuilder.notEqual(root.get("id"), currentUerId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
