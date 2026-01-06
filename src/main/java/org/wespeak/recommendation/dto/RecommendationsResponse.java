package org.wespeak.recommendation.dto;

import java.time.Instant;
import java.util.List;
import lombok.Builder;

@Builder
public record RecommendationsResponse(
    List<RecommendationItemDto> recommendations, Instant generatedAt) {}
