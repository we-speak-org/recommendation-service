package org.wespeak.recommendation.dto;

import lombok.Builder;

@Builder
public record ContextInfoDto(Integer totalSkillGaps, Integer daysSinceLastLesson, String recommendationGenerated) {}
