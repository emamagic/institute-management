package com.emamagic.institutemanagement.feature.user.role;

import com.emamagic.institutemanagement.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByType(RoleType name);
}
