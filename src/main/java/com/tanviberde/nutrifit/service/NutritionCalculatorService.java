package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.entity.User;

public interface NutritionCalculatorService {

    void calculateAndSetTargets(User user);
}