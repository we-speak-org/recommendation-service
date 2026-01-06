package org.wespeak.recommendation.messaging.payload;

import java.util.List;

public record LessonCompletedPayload(
    String userId, String targetLanguageCode, String lessonId, Integer score, List<String> skillsAcquired) {}
