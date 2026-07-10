package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.GroceryList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long> {

    Page<GroceryList> findByUserIdAndDeletedFalseOrderByGeneratedDateDesc(
            Long userId, Pageable pageable);
}