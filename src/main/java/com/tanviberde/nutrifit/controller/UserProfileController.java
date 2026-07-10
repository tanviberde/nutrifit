package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.user.UserProfileRequest;
import com.tanviberde.nutrifit.dto.user.UserProfileResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PutMapping
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserProfileRequest request) {
        UserProfileResponse response = userProfileService.updateProfile(principal.getId(), request);
        return ApiResponse.success("Profile updated successfully", response);
    }

    @GetMapping
    public ApiResponse<UserProfileResponse> getProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        UserProfileResponse response = userProfileService.getProfile(principal.getId());
        return ApiResponse.success(response);
    }
}