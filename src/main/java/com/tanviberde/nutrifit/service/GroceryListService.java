package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.grocery.GroceryItemResponse;
import com.tanviberde.nutrifit.dto.grocery.GroceryListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroceryListService {

    GroceryListResponse generateFromRecipe(Long userId, Long recipeId);

    Page<GroceryListResponse> getGroceryLists(Long userId, Pageable pageable);

    GroceryItemResponse toggleItemPurchased(Long userId, Long groceryListId, Long itemId);
}