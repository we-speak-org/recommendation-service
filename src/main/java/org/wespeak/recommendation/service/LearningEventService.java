package org.wespeak.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wespeak.recommendation.messaging.payload.ConversationCompletedPayload;
import org.wespeak.recommendation.messaging.payload.FeedbackGeneratedPayload;
import org.wespeak.recommendation.messaging.payload.GamificationEventPayload;
import org.wespeak.recommendation.messaging.payload.LessonCompletedPayload;
import org.wespeak.recommendation.repository.LearnerProfileRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearningEventService {

  private final LearnerProfileRepository learnerProfileRepository;

  public void handleLessonCompleted(LessonCompletedPayload payload) {
    log.info(
        "Processing lesson completion user={} lang={} lesson={}",
        payload.userId(),
        payload.targetLanguageCode(),
        payload.lessonId());
    learnerProfileRepository
        .findByUserIdAndTargetLanguageCode(payload.userId(), payload.targetLanguageCode())
        .ifPresent(profile -> profile.setLastUpdated(java.time.Instant.now()));
  }

  public void handleFeedbackGenerated(FeedbackGeneratedPayload payload) {
    log.info(
        "Processing feedback generated user={} lang={} errors={}",
        payload.userId(),
        payload.targetLanguageCode(),
        payload.errorsDetected());
  }

  public void handleConversationCompleted(ConversationCompletedPayload payload) {
    log.info(
        "Processing conversation completed user={} lang={} topic={}",
        payload.userId(),
        payload.targetLanguageCode(),
        payload.topicCode());
  }

  public void handleGamificationEvent(GamificationEventPayload payload) {
    log.info(
        "Processing gamification event user={} lang={} type={}",
        payload.userId(),
        payload.targetLanguageCode(),
        payload.eventType());
  }
}
