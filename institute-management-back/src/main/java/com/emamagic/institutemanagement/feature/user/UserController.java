package com.emamagic.institutemanagement.feature.user;

import com.emamagic.institutemanagement.common.PageResponse;
import com.emamagic.institutemanagement.feature.user.dto.AdminUpdateRequest;
import com.emamagic.institutemanagement.feature.user.dto.UserResponse;
import com.emamagic.institutemanagement.feature.user.dto.UserSearch;
import com.emamagic.institutemanagement.feature.user.dto.UserUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {
    private final UserService svc;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(svc.me());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponse>> users() {
        return ResponseEntity.ok(svc.findAll());
    }

    //todo merge these two below apis(profile)
    @PatchMapping("/profile")
//    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal(expression = "id") Long id,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return ResponseEntity.ok(svc.updateUser(id, request, false));
    }

    @PatchMapping("/profile/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> updateAsAdmin(
            @PathVariable Long id,
            @RequestBody @Valid AdminUpdateRequest request) {
        return ResponseEntity.ok(svc.updateUser(id, request, true));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> search(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestBody @Valid UserSearch req
    ) {
        return ResponseEntity.ok(svc.search((page - 1), size, req));
    }
}
