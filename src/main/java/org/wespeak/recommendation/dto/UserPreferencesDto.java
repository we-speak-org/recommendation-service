package org.wespeak.recommendation.dto;

import java.time.Instant;
import java.util.List;
import lombok.Builder;

@Builder
public record UserPreferencesDto(
    String id,
    String userId,
    String targetLanguageCode,
    List<String> preferredLearningTime,
    Integer dailyGoalMinutes,
    List<String> focusAreas,
    List<String> excludedTopics,
    Instant updatedAt) {}
