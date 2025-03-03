package com.emamagic.institutemanagement.feature.user;

import com.emamagic.institutemanagement.common.AppMapper;
import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.entity.UserRole;
import com.emamagic.institutemanagement.feature.user.dto.AdminUpdateRequest;
import com.emamagic.institutemanagement.feature.user.dto.UserResponse;
import com.emamagic.institutemanagement.feature.user.dto.UserSearch;
import com.emamagic.institutemanagement.feature.user.dto.UserUpdateRequest;
import com.emamagic.institutemanagement.feature.user.role.RoleType;
import com.emamagic.institutemanagement.feature.user.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final UserRoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return repo.findById(Long.valueOf(id))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserResponse me() {
        UserApp user = (UserApp) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return AppMapper.toResponse(user);
    }

    // todo: implement pagination
    @Override
    public List<UserResponse> findAll() {
        UserApp admin = (UserApp) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return repo.findAllExcludingCurrent(admin.getId()).stream()
                .map(AppMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest req, boolean isAdmin) {
        UserApp user = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (req.getName() != null && !req.getName().isBlank()) user.setName(req.getName());
        if (req.getAge() != null && !req.getAge().isBlank()) user.setAge(req.getAge());
        if (req.getGender() != null && !req.getGender().isBlank())
            user.setGender(Gender.valueOf(req.getGender().toUpperCase()));

        if (isAdmin && req instanceof AdminUpdateRequest adminRequest) {
            if (adminRequest.getIsApproved() != null) user.setIsApproved(adminRequest.getIsApproved());
            //todo: changing the role, has a lots of considerations
            if (adminRequest.getRole() != null && !adminRequest.getRole().isBlank()) {
                UserRole role = roleRepo.findByType(RoleType.valueOf(adminRequest.getRole().toUpperCase()))
                        .orElseThrow(() -> new NoSuchElementException("Role not found"));
                user.setRole(role);
            }
            if (adminRequest.getEmail() != null && !adminRequest.getEmail().isBlank()) {
                Optional<UserApp> userOtp = repo.findByEmail(adminRequest.getEmail());
                if (userOtp.isPresent()) {
                    throw new RuntimeException("There is a user with this email");
                }

                user.setEmail(adminRequest.getEmail());
            }
        }

        return AppMapper.toResponse(user);
    }

    @Transactional
    @Override
    public void updateAdminPassword() {
        repo.updateAdminPassword(passwordEncoder.encode("123"));
    }

    @Override
    public PageResponse<UserResponse> search(int page, int size, UserSearch req) {
        UserApp currentUser = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Specification<UserApp> spec = UserSpecification.findByCriteria(req, currentUser.getId());
        Page<UserApp> users = repo.findAll(spec, pageable);
        List<UserResponse> userResponses = users.stream()
                .map(AppMapper::toResponse)
                .toList();

        return new PageResponse<>(
                userResponses,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }

}
