package org.wespeak.recommendation.dto;

import lombok.Builder;

@Builder
public record TargetedSkillDto(String skillId, String skillCode, Integer currentMastery) {}
