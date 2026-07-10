package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.meal.MealRequest;
import com.tanviberde.nutrifit.dto.meal.MealResponse;
import com.tanviberde.nutrifit.entity.Meal;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.exception.UnauthorizedException;
import com.tanviberde.nutrifit.repository.MealRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

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
        return MealResponse.fromEntity(savedMeal);
    }

    @Override
    public MealResponse updateMeal(Long userId, Long mealId, MealRequest request) {
        Meal meal = getOwnedMeal(userId, mealId);

        meal.setMealName(request.getMealName());
        meal.setCalories(request.getCalories());
        meal.setProtein(request.getProtein());
        meal.setCarbs(request.getCarbs());
        meal.setFat(request.getFat());
        meal.setFibre(request.getFibre());
        meal.setMealDate(request.getMealDate());

        Meal updatedMeal = mealRepository.save(meal);
        return MealResponse.fromEntity(updatedMeal);
    }

    @Override
    public void deleteMeal(Long userId, Long mealId) {
        Meal meal = getOwnedMeal(userId, mealId);
        meal.setDeleted(true);
        mealRepository.save(meal);
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