package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Optional<Recipe> findTopByUserIdAndDeletedFalseOrderByCreatedAtDesc(Long userId);
}