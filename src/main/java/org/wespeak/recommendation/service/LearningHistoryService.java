package org.wespeak.recommendation.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wespeak.recommendation.messaging.payload.FeedbackGeneratedPayload;
import org.wespeak.recommendation.messaging.payload.LessonCompletedPayload;
import org.wespeak.recommendation.messaging.payload.UserProfileUpdatedPayload;
import org.wespeak.recommendation.model.entity.LearningHistory;
import org.wespeak.recommendation.repository.LearningHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearningHistoryService {

  private final LearningHistoryRepository learningHistoryRepository;

  @Transactional
  public LearningHistory getOrCreate(String userId, String language) {
    return learningHistoryRepository
        .findByUserIdAndTargetLanguageCode(userId, language)
        .orElseGet(
            () ->
                learningHistoryRepository.save(
                    LearningHistory.builder()
                        .userId(userId)
                        .targetLanguageCode(language)
                        .completedLessonIds(new ArrayList<>())
                        .weakAreas(new ArrayList<>())
                        .strongAreas(new ArrayList<>())
                        .averageScore(0.0)
                        .totalLessons(0)
                        .totalConversationMinutes(0)
                        .lastUpdated(Instant.now())
                        .build()));
  }

  public void onLessonCompleted(LessonCompletedPayload payload) {
    LearningHistory history =
        getOrCreate(payload.userId(), payload.targetLanguageCode());

    List<String> completed = history.getCompletedLessonIds() == null ? new ArrayList<>() : history.getCompletedLessonIds();
    if (!completed.contains(payload.lessonId())) {
      completed.add(payload.lessonId());
      history.setCompletedLessonIds(completed);
    }
    history.setLastLessonId(payload.lessonId());
    history.setTotalLessons(Optional.ofNullable(history.getTotalLessons()).orElse(0) + 1);
    double currentAvg = Optional.ofNullable(history.getAverageScore()).orElse(0.0);
    int total = history.getTotalLessons();
    history.setAverageScore(((currentAvg * (total - 1)) + payload.score()) / total);
    history.setLastUpdated(Instant.now());
    learningHistoryRepository.save(history);
  }

  public void onFeedbackGenerated(FeedbackGeneratedPayload payload) {
    LearningHistory history =
        getOrCreate(payload.userId(), payload.targetLanguageCode());
    List<LearningHistory.WeakArea> weakAreas =
        history.getWeakAreas() == null ? new ArrayList<>() : new ArrayList<>(history.getWeakAreas());

    if (payload.analysis() != null && payload.analysis().errors() != null) {
      payload
          .analysis()
          .errors()
          .forEach(
              err -> {
                LearningHistory.WeakArea area =
                    weakAreas.stream()
                        .filter(
                            a ->
                                a.getCategory().equals(err.category())
                                    && a.getSubcategory().equals(err.subcategory()))
                        .findFirst()
                        .orElseGet(
                            () -> {
                              LearningHistory.WeakArea created =
                                  LearningHistory.WeakArea.builder()
                                      .category(err.category())
                                      .subcategory(err.subcategory())
                                      .errorCount(0)
                                      .lastErrorAt(Instant.now())
                                      .build();
                              weakAreas.add(created);
                              return created;
                            });
                area.setErrorCount(area.getErrorCount() + err.count());
                area.setLastErrorAt(Instant.now());
              });
    }

    if (payload.analysis() != null && payload.analysis().strengths() != null) {
      List<LearningHistory.StrongArea> strongAreas =
          history.getStrongAreas() == null ? new ArrayList<>() : new ArrayList<>(history.getStrongAreas());
      payload
          .analysis()
          .strengths()
          .forEach(
              st -> {
                strongAreas.removeIf(
                    s ->
                        s.getCategory().equals(st.category())
                            && s.getSubcategory().equals(st.subcategory()));
                strongAreas.add(
                    LearningHistory.StrongArea.builder()
                        .category(st.category())
                        .subcategory(st.subcategory())
                        .successRate(st.score())
                        .build());
              });
      history.setStrongAreas(strongAreas);
    }

    history.setWeakAreas(weakAreas);
    history.setLastUpdated(Instant.now());
    learningHistoryRepository.save(history);
  }

  public void onUserProfileUpdated(UserProfileUpdatedPayload payload) {
    if (payload.changes() == null || payload.changes().learningProfiles() == null) {
      return;
    }
    payload
        .changes()
        .learningProfiles()
        .forEach(
            lp -> {
              LearningHistory history = getOrCreate(payload.userId(), lp.targetLanguageCode());
              history.setLastUpdated(Instant.now());
              learningHistoryRepository.save(history);
            });
  }
}
