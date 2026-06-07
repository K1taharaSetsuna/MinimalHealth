package com.minimalhealth.repository;

import com.minimalhealth.model.entity.AiSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AiSuggestionRepository extends JpaRepository<AiSuggestion, Long> {
    List<AiSuggestion> findByUserIdAndGeneratedDateAndIsActiveTrueOrderByCreatedAtAsc(
        Long userId, LocalDate generatedDate
    );
}
