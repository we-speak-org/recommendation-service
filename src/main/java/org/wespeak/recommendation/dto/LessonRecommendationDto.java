package org.wespeak.recommendation.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record LessonRecommendationDto(
    String lessonId,
    String titleKey,
    String type,
    String level,
    Integer estimatedDuration,
    Double score,
    String reason,
    String reasonKey,
    List<TargetedSkillDto> targetedSkills) {}
