package org.wespeak.recommendation.messaging.payload;

public record LessonCompletedPayload(
    String lessonId, String userId, String targetLanguageCode, Integer score, boolean isPerfect) {}
