package com.emamagic.institutemanagement.feature.user.role;

import com.emamagic.institutemanagement.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository repo;

    @Override
    public List<String> findAll() {
        return repo.findAll().stream().map(UserRole::getName).toList();
    }
}
