package com.emamagic.institutemanagement.feature.user;

import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.feature.user.dto.UserResponse;
import com.emamagic.institutemanagement.feature.user.dto.UserSearch;
import com.emamagic.institutemanagement.feature.user.dto.UserUpdateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserResponse me();
    List<UserResponse> findAll();
    UserResponse updateUser(Long id, UserUpdateRequest req, boolean isAdmin);
    PageResponse<UserResponse> search(int page, int size, UserSearch req);
    void updateAdminPassword();
}
