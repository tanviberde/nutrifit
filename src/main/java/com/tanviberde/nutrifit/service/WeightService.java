package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.weight.WeightRequest;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;

import java.util.List;

public interface WeightService {

    WeightResponse createWeightEntry(Long userId, WeightRequest request);

    WeightResponse updateWeightEntry(Long userId, Long weightEntryId, WeightRequest request);

    void deleteWeightEntry(Long userId, Long weightEntryId);

    List<WeightResponse> getWeightEntries(Long userId);
}