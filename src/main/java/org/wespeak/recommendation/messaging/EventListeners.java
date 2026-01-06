package org.wespeak.recommendation.messaging;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wespeak.recommendation.messaging.payload.FeedbackGeneratedPayload;
import org.wespeak.recommendation.messaging.payload.LessonCompletedPayload;
import org.wespeak.recommendation.messaging.payload.UserProfileUpdatedPayload;
import org.wespeak.recommendation.service.LearningHistoryService;
import org.wespeak.recommendation.service.RecommendationFacade;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EventListeners {

  private final LearningHistoryService learningHistoryService;
  private final RecommendationFacade recommendationFacade;

  @Bean
  public Consumer<CloudEvent<LessonCompletedPayload>> lessonCompletedListener() {
    return event -> {
      log.info("Received lesson.completed {}", event.getId());
      learningHistoryService.onLessonCompleted(event.getData());
      recommendationFacade.recomputeForUser(
          event.getData().userId(), event.getData().targetLanguageCode());
    };
  }

  @Bean
  public Consumer<CloudEvent<FeedbackGeneratedPayload>> feedbackGeneratedListener() {
    return event -> {
      log.info("Received feedback.generated {}", event.getId());
      learningHistoryService.onFeedbackGenerated(event.getData());
      recommendationFacade.recomputeForUser(
          event.getData().userId(), event.getData().targetLanguageCode());
    };
  }

  @Bean
  public Consumer<CloudEvent<UserProfileUpdatedPayload>> userProfileUpdatedListener() {
    return event -> {
      log.info("Received user.profile.updated {}", event.getId());
      learningHistoryService.onUserProfileUpdated(event.getData());
      recommendationFacade.recomputeForUser(
          event.getData().userId(), targetLanguage(event.getData()));
    };
  }

  private String targetLanguage(UserProfileUpdatedPayload payload) {
    if (payload.changes() != null
        && payload.changes().learningProfiles() != null
        && !payload.changes().learningProfiles().isEmpty()) {
      return payload.changes().learningProfiles().getFirst().targetLanguageCode();
    }
    return null;
  }
}
