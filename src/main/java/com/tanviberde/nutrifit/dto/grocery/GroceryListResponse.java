package com.tanviberde.nutrifit.dto.grocery;

import com.tanviberde.nutrifit.entity.GroceryItem;
import com.tanviberde.nutrifit.entity.GroceryList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroceryListResponse {

    private Long id;
    private Long recipeId;
    private String recipeTitle;
    private LocalDate generatedDate;
    private List<GroceryItemResponse> items;

    public static GroceryListResponse fromEntity(
            GroceryList groceryList, List<GroceryItem> items, String recipeTitle) {
        return GroceryListResponse.builder()
                .id(groceryList.getId())
                .recipeId(groceryList.getRecipe() != null ? groceryList.getRecipe().getId() : null)
                .recipeTitle(recipeTitle)
                .generatedDate(groceryList.getGeneratedDate())
                .items(items.stream().map(GroceryItemResponse::fromEntity).toList())
                .build();
    }
}