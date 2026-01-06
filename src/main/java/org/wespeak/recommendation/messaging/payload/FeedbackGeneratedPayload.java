package org.wespeak.recommendation.messaging.payload;

import java.util.List;

public record FeedbackGeneratedPayload(
    String userId, String targetLanguageCode, List<String> errorsDetected, List<String> skillGapsIdentified) {}
