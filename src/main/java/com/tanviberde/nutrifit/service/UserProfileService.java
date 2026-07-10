package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.user.UserProfileRequest;
import com.tanviberde.nutrifit.dto.user.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse updateProfile(Long userId, UserProfileRequest request);

    UserProfileResponse getProfile(Long userId);
}