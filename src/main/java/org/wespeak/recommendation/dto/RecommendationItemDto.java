package org.wespeak.recommendation.dto;

import java.time.Instant;
import java.util.Map;
import lombok.Builder;

@Builder
public record RecommendationItemDto(
    String id,
    String type,
    String targetId,
    String targetType,
    String title,
    String reason,
    Integer priority,
    Map<String, Object> metadata,
    Instant expiresAt,
    Instant clickedAt) {}
