package org.wespeak.recommendation.messaging;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wespeak.recommendation.messaging.payload.ConversationCompletedPayload;
import org.wespeak.recommendation.messaging.payload.FeedbackGeneratedPayload;
import org.wespeak.recommendation.messaging.payload.GamificationEventPayload;
import org.wespeak.recommendation.messaging.payload.LessonCompletedPayload;
import org.wespeak.recommendation.service.LearningEventService;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EventListeners {

  private final LearningEventService learningEventService;

  @Bean
  public Consumer<CloudEvent<LessonCompletedPayload>> lessonCompletedListener() {
    return event -> {
      log.info("Received lesson.completed event {}", event.getId());
      learningEventService.handleLessonCompleted(event.getData());
    };
  }

  @Bean
  public Consumer<CloudEvent<FeedbackGeneratedPayload>> feedbackGeneratedListener() {
    return event -> {
      log.info("Received feedback.generated event {}", event.getId());
      learningEventService.handleFeedbackGenerated(event.getData());
    };
  }

  @Bean
  public Consumer<CloudEvent<ConversationCompletedPayload>> conversationCompletedListener() {
    return event -> {
      log.info("Received conversation.completed event {}", event.getId());
      learningEventService.handleConversationCompleted(event.getData());
    };
  }

  @Bean
  public Consumer<CloudEvent<GamificationEventPayload>> gamificationEventListener() {
    return event -> {
      log.info("Received gamification event {}", event.getId());
      learningEventService.handleGamificationEvent(event.getData());
    };
  }
}
