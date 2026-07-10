package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    List<Allergy> findByUserIdAndDeletedFalse(Long userId);
}