package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.grocery.GroceryItemResponse;
import com.tanviberde.nutrifit.dto.grocery.GroceryListResponse;
import com.tanviberde.nutrifit.entity.GroceryItem;
import com.tanviberde.nutrifit.entity.GroceryList;
import com.tanviberde.nutrifit.entity.Recipe;
import com.tanviberde.nutrifit.entity.RecipeIngredient;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.exception.UnauthorizedException;
import com.tanviberde.nutrifit.repository.GroceryItemRepository;
import com.tanviberde.nutrifit.repository.GroceryListRepository;
import com.tanviberde.nutrifit.repository.RecipeIngredientRepository;
import com.tanviberde.nutrifit.repository.RecipeRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.service.GroceryListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroceryListServiceImpl implements GroceryListService {

    private final GroceryListRepository groceryListRepository;
    private final GroceryItemRepository groceryItemRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final UserRepository userRepository;

    @Override
    public GroceryListResponse generateFromRecipe(Long userId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to access this recipe");
        }

        User userRef = userRepository.getReferenceById(userId);
        List<RecipeIngredient> recipeIngredients =
                recipeIngredientRepository.findByRecipeIdAndDeletedFalse(recipeId);

        GroceryList groceryList = GroceryList.builder()
                .user(userRef)
                .recipe(recipe)
                .generatedDate(LocalDate.now())
                .build();

        GroceryList savedGroceryList = groceryListRepository.save(groceryList);

        List<GroceryItem> items = recipeIngredients.stream()
                .map(ingredient -> GroceryItem.builder()
                        .groceryList(savedGroceryList)
                        .itemName(ingredient.getIngredientName())
                        .quantity(ingredient.getQuantity())
                        .unit(ingredient.getUnit())
                        .build())
                .toList();

        List<GroceryItem> savedItems = groceryItemRepository.saveAll(items);

        return GroceryListResponse.fromEntity(savedGroceryList, savedItems, recipe.getTitle());
    }

    @Override
    public Page<GroceryListResponse> getGroceryLists(Long userId, Pageable pageable) {
        return groceryListRepository.findByUserIdAndDeletedFalseOrderByGeneratedDateDesc(userId, pageable)
                .map(groceryList -> {
                    List<GroceryItem> items =
                            groceryItemRepository.findByGroceryListIdAndDeletedFalse(groceryList.getId());
                    String recipeTitle = groceryList.getRecipe() != null
                            ? groceryList.getRecipe().getTitle()
                            : null;
                    return GroceryListResponse.fromEntity(groceryList, items, recipeTitle);
                });
    }

    @Override
    public GroceryItemResponse toggleItemPurchased(Long userId, Long groceryListId, Long itemId) {
        GroceryItem item = groceryItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery item not found"));

        if (!item.getGroceryList().getId().equals(groceryListId)) {
            throw new ResourceNotFoundException("Grocery item does not belong to this list");
        }

        if (!item.getGroceryList().getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to access this grocery item");
        }

        item.setPurchased(!item.isPurchased());
        GroceryItem updatedItem = groceryItemRepository.save(item);

        return GroceryItemResponse.fromEntity(updatedItem);
    }
}