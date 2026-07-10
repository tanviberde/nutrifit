package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.weight.WeightRequest;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.WeightEntry;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.exception.UnauthorizedException;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WeightEntryRepository;
import com.tanviberde.nutrifit.service.WeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeightServiceImpl implements WeightService {

    private final WeightEntryRepository weightEntryRepository;
    private final UserRepository userRepository;

    @Override
    public WeightResponse createWeightEntry(Long userId, WeightRequest request) {
        User userRef = userRepository.getReferenceById(userId);

        WeightEntry entry = WeightEntry.builder()
                .user(userRef)
                .weightKg(request.getWeightKg())
                .entryDate(request.getEntryDate())
                .build();

        WeightEntry savedEntry = weightEntryRepository.save(entry);
        return WeightResponse.fromEntity(savedEntry);
    }

    @Override
    public WeightResponse updateWeightEntry(Long userId, Long weightEntryId, WeightRequest request) {
        WeightEntry entry = getOwnedWeightEntry(userId, weightEntryId);

        entry.setWeightKg(request.getWeightKg());
        entry.setEntryDate(request.getEntryDate());

        WeightEntry updatedEntry = weightEntryRepository.save(entry);
        return WeightResponse.fromEntity(updatedEntry);
    }

    @Override
    public void deleteWeightEntry(Long userId, Long weightEntryId) {
        WeightEntry entry = getOwnedWeightEntry(userId, weightEntryId);
        entry.setDeleted(true);
        weightEntryRepository.save(entry);
    }

    @Override
    public List<WeightResponse> getWeightEntries(Long userId) {
        return weightEntryRepository.findByUserIdAndDeletedFalse(userId).stream()
                .map(WeightResponse::fromEntity)
                .toList();
    }

    private WeightEntry getOwnedWeightEntry(Long userId, Long weightEntryId) {
        WeightEntry entry = weightEntryRepository.findById(weightEntryId)
                .orElseThrow(() -> new ResourceNotFoundException("Weight entry not found"));

        if (!entry.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to access this weight entry");
        }

        return entry;
    }
}