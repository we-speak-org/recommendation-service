package org.wespeak.recommendation.messaging.payload;

public record GamificationEventPayload(
    String userId, String targetLanguageCode, String eventType, Integer value) {}
