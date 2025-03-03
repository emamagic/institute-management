package com.emamagic.institutemanagement.feature.user;

import com.emamagic.institutemanagement.entity.UserApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserApp, Long>, JpaSpecificationExecutor<UserApp> {
    Optional<UserApp> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isVerified=:isVerificationCompleted WHERE u.id = :userId")
    void updateVerificationCodeCompletion(
            @Param("userId") Long userId,
            @Param("isVerificationCompleted") boolean isVerificationCompleted
    );

    @Query("FROM User u WHERE u.id!=:id ORDER BY u.createdAt ASC")
    List<UserApp> findAllExcludingCurrent(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.hashedPassword=:hashedPassword WHERE u.role.id=1")
    void updateAdminPassword(String hashedPassword);
}
