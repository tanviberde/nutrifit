package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {

    List<GroceryItem> findByGroceryListIdAndDeletedFalse(Long groceryListId);
}