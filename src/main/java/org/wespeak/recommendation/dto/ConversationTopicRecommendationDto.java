package org.wespeak.recommendation.dto;

import lombok.Builder;

@Builder
public record ConversationTopicRecommendationDto(
    String topicCode, String titleKey, String level, Integer estimatedDuration, Double score) {}
