package org.wespeak.recommendation.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record NextLessonResponse(
    LessonRecommendationDto recommendation,
    List<AlternativeRecommendationDto> alternatives,
    ContextInfoDto contextInfo) {}
