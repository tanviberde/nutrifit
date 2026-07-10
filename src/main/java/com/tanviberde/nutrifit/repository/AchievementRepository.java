package com.tanviberde.nutrifit.repository;

import com.tanviberde.nutrifit.entity.Achievement;
import com.tanviberde.nutrifit.entity.enums.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    List<Achievement> findByUserIdAndDeletedFalse(Long userId);

    boolean existsByUserIdAndAchievementTypeAndTitleAndDeletedFalse(
            Long userId, AchievementType achievementType, String title);
}