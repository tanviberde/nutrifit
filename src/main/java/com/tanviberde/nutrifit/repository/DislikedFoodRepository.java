package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.DislikedFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DislikedFoodRepository extends JpaRepository<DislikedFood, Long> {

    List<DislikedFood> findByUserIdAndDeletedFalse(Long userId);
}