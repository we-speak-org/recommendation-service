package org.wespeak.recommendation.dto;

import lombok.Builder;

@Builder
public record AlternativeRecommendationDto(
    String lessonId, String titleKey, Double score, String reason) {}
