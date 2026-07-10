package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.weight.WeightRequest;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.WeightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weight")
@RequiredArgsConstructor
public class WeightController {

    private final WeightService weightService;

    @PostMapping
    public ResponseEntity<ApiResponse<WeightResponse>> createWeightEntry(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody WeightRequest request) {
        WeightResponse response = weightService.createWeightEntry(principal.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Weight entry logged successfully", response),
                HttpStatus.CREATED);
    }

    @PutMapping("/{weightEntryId}")
    public ApiResponse<WeightResponse> updateWeightEntry(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long weightEntryId,
            @Valid @RequestBody WeightRequest request) {
        WeightResponse response =
                weightService.updateWeightEntry(principal.getId(), weightEntryId, request);
        return ApiResponse.success("Weight entry updated successfully", response);
    }

    @DeleteMapping("/{weightEntryId}")
    public ApiResponse<Void> deleteWeightEntry(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long weightEntryId) {
        weightService.deleteWeightEntry(principal.getId(), weightEntryId);
        return ApiResponse.success("Weight entry deleted successfully", null);
    }

    @GetMapping
    public ApiResponse<List<WeightResponse>> getWeightEntries(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<WeightResponse> response = weightService.getWeightEntries(principal.getId());
        return ApiResponse.success(response);
    }
}