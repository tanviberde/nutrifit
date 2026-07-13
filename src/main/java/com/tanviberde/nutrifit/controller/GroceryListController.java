package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.common.PageResponse;
import com.tanviberde.nutrifit.dto.grocery.GroceryItemResponse;
import com.tanviberde.nutrifit.dto.grocery.GroceryListResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.GroceryListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grocery-lists")
@RequiredArgsConstructor
public class GroceryListController {

    private final GroceryListService groceryListService;

    @PostMapping("/from-recipe/{recipeId}")
    public ResponseEntity<ApiResponse<GroceryListResponse>> generateFromRecipe(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long recipeId) {
        GroceryListResponse response = groceryListService.generateFromRecipe(principal.getId(), recipeId);
        return new ResponseEntity<>(
                ApiResponse.success("Grocery list generated successfully", response),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ApiResponse<PageResponse<GroceryListResponse>> getGroceryLists(
            @AuthenticationPrincipal UserPrincipal principal,
            Pageable pageable) {
        PageResponse<GroceryListResponse> response =
                PageResponse.fromPage(groceryListService.getGroceryLists(principal.getId(), pageable));
        return ApiResponse.success(response);
    }

    @PatchMapping("/{groceryListId}/items/{itemId}/toggle")
    public ApiResponse<GroceryItemResponse> toggleItemPurchased(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long groceryListId,
            @PathVariable Long itemId) {
        GroceryItemResponse response =
                groceryListService.toggleItemPurchased(principal.getId(), groceryListId, itemId);
        return ApiResponse.success(response);
    }
}