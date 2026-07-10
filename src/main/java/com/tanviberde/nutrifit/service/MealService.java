package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.meal.MealRequest;
import com.tanviberde.nutrifit.dto.meal.MealResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealService {

    MealResponse createMeal(Long userId, MealRequest request);

    MealResponse updateMeal(Long userId, Long mealId, MealRequest request);

    void deleteMeal(Long userId, Long mealId);

    Page<MealResponse> getMeals(Long userId, Pageable pageable);
}