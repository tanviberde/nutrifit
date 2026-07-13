package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.meal.MealRequest;
import com.tanviberde.nutrifit.dto.meal.MealResponse;
import com.tanviberde.nutrifit.entity.Meal;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.exception.UnauthorizedException;
import com.tanviberde.nutrifit.repository.MealRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.service.ActivityTrackingService;
import com.tanviberde.nutrifit.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.tanviberde.nutrifit.service.GamificationService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final ActivityTrackingService activityTrackingService;
    private final GamificationService gamificationService;

    @Override
    public MealResponse createMeal(Long userId, MealRequest request) {
        User userRef = userRepository.getReferenceById(userId);

        Meal meal = Meal.builder()
                .user(userRef)
                .mealName(request.getMealName())
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbs(request.getCarbs())
                .fat(request.getFat())
                .fibre(request.getFibre())
                .mealDate(request.getMealDate())
                .build();

        Meal savedMeal = mealRepository.save(meal);

        activityTrackingService.recalculateDailyProgress(userId, savedMeal.getMealDate());
        activityTrackingService.logMealActivity(userId, savedMeal.getMealDate());
        gamificationService.checkAndAwardAchievements(userId);

        return MealResponse.fromEntity(savedMeal);
    }

    @Override
    public MealResponse updateMeal(Long userId, Long mealId, MealRequest request) {
        Meal meal = getOwnedMeal(userId, mealId);
        LocalDate oldDate = meal.getMealDate();

        meal.setMealName(request.getMealName());
        meal.setCalories(request.getCalories());
        meal.setProtein(request.getProtein());
        meal.setCarbs(request.getCarbs());
        meal.setFat(request.getFat());
        meal.setFibre(request.getFibre());
        meal.setMealDate(request.getMealDate());

        Meal updatedMeal = mealRepository.save(meal);

        activityTrackingService.recalculateDailyProgress(userId, oldDate);
        if (!oldDate.equals(updatedMeal.getMealDate())) {
            activityTrackingService.recalculateDailyProgress(userId, updatedMeal.getMealDate());
        }
        activityTrackingService.logMealActivity(userId, updatedMeal.getMealDate());

        return MealResponse.fromEntity(updatedMeal);
    }

    @Override
    public void deleteMeal(Long userId, Long mealId) {
        Meal meal = getOwnedMeal(userId, mealId);
        LocalDate mealDate = meal.getMealDate();

        meal.setDeleted(true);
        mealRepository.save(meal);

        activityTrackingService.recalculateDailyProgress(userId, mealDate);
    }

    @Override
    public Page<MealResponse> getMeals(Long userId, Pageable pageable) {
        return mealRepository.findByUserIdAndDeletedFalse(userId, pageable)
                .map(MealResponse::fromEntity);
    }

    private Meal getOwnedMeal(Long userId, Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));

        if (!meal.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to access this meal");
        }

        return meal;
    }
}