package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.user.UserProfileRequest;
import com.tanviberde.nutrifit.dto.user.UserProfileResponse;
import com.tanviberde.nutrifit.entity.Allergy;
import com.tanviberde.nutrifit.entity.DislikedFood;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.repository.AllergyRepository;
import com.tanviberde.nutrifit.repository.DislikedFoodRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.service.NutritionCalculatorService;
import com.tanviberde.nutrifit.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final AllergyRepository allergyRepository;
    private final DislikedFoodRepository dislikedFoodRepository;
    private final NutritionCalculatorService nutritionCalculatorService;

    @Override
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setHeightCm(request.getHeightCm());
        user.setWeightKg(request.getWeightKg());
        user.setActivityLevel(request.getActivityLevel());
        user.setFitnessGoal(request.getFitnessGoal());
        user.setDietPreference(request.getDietPreference());

        nutritionCalculatorService.calculateAndSetTargets(user);
        User savedUser = userRepository.save(user);

        List<String> allergyNames = replaceAllergies(userId, request.getAllergies());
        List<String> dislikedFoodNames = replaceDislikedFoods(userId, request.getDislikedFoods());

        return UserProfileResponse.fromEntity(savedUser, allergyNames, dislikedFoodNames);
    }

    @Override
    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<String> allergyNames = allergyRepository.findByUserIdAndDeletedFalse(userId).stream()
                .map(Allergy::getName)
                .toList();

        List<String> dislikedFoodNames = dislikedFoodRepository.findByUserIdAndDeletedFalse(userId).stream()
                .map(DislikedFood::getName)
                .toList();

        return UserProfileResponse.fromEntity(user, allergyNames, dislikedFoodNames);
    }

    private List<String> replaceAllergies(Long userId, List<String> allergyNames) {
        allergyRepository.deleteAll(allergyRepository.findByUserIdAndDeletedFalse(userId));

        if (allergyNames == null || allergyNames.isEmpty()) {
            return Collections.emptyList();
        }

        User userRef = userRepository.getReferenceById(userId);
        List<Allergy> allergies = allergyNames.stream()
                .map(name -> Allergy.builder().user(userRef).name(name).build())
                .toList();
        allergyRepository.saveAll(allergies);

        return allergyNames;
    }

    private List<String> replaceDislikedFoods(Long userId, List<String> dislikedFoodNames) {
        dislikedFoodRepository.deleteAll(dislikedFoodRepository.findByUserIdAndDeletedFalse(userId));

        if (dislikedFoodNames == null || dislikedFoodNames.isEmpty()) {
            return Collections.emptyList();
        }

        User userRef = userRepository.getReferenceById(userId);
        List<DislikedFood> dislikedFoods = dislikedFoodNames.stream()
                .map(name -> DislikedFood.builder().user(userRef).name(name).build())
                .toList();
        dislikedFoodRepository.saveAll(dislikedFoods);

        return dislikedFoodNames;
    }
}