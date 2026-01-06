package org.wespeak.recommendation.messaging.payload;

public record ConversationCompletedPayload(
    String userId, String targetLanguageCode, String topicCode, Integer averageScore) {}
