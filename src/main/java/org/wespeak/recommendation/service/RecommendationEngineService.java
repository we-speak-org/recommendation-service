package org.wespeak.recommendation.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wespeak.recommendation.client.ConversationServiceClient;
import org.wespeak.recommendation.client.LessonServiceClient;
import org.wespeak.recommendation.model.entity.LearningHistory;
import org.wespeak.recommendation.model.entity.Recommendation;
import org.wespeak.recommendation.model.entity.UserPreferences;
import org.wespeak.recommendation.repository.RecommendationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationEngineService {

  private static final int MAX_RECOMMENDATIONS = 5;
  private static final int REVISION_THRESHOLD = 5;

  private final RecommendationRepository recommendationRepository;
  private final LessonServiceClient lessonServiceClient;
  private final ConversationServiceClient conversationServiceClient;

  public List<Recommendation> generateNextLesson(
      String userId, String language, LearningHistory history) {
    LessonServiceClient.ProgressResponse progress =
        lessonServiceClient.getProgress(userId, language);
    if (progress == null
        || progress.nextLesson() == null
        || !progress.nextLesson().prerequisitesMet()) {
      return List.of();
    }
    LessonServiceClient.Lesson lesson = progress.nextLesson();
    Recommendation rec =
        Recommendation.builder()
            .userId(userId)
            .targetLanguageCode(language)
            .type(Recommendation.RecommendationType.next_lesson)
            .targetId(lesson.id())
            .targetType(Recommendation.TargetType.lesson)
            .title(lesson.title())
            .reason(
                progress.unitUnlocked()
                    ? "Nouvelle unité débloquée"
                    : "Continuez votre progression")
            .priority(1)
            .metadata(
                Map.of(
                    "unitName", Objects.toString(lesson.unitName(), ""),
                    "estimatedMinutes", Optional.ofNullable(lesson.estimatedMinutes()).orElse(10)))
            .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
            .build();
    return List.of(rec);
  }

  public List<Recommendation> generateRevision(
      String userId, String language, LearningHistory history) {
    List<Recommendation> recs = new ArrayList<>();
    if (history.getWeakAreas() == null) return recs;
    List<LearningHistory.WeakArea> sorted =
        history.getWeakAreas().stream()
            .filter(a -> a.getErrorCount() != null && a.getErrorCount() >= REVISION_THRESHOLD)
            .sorted(Comparator.comparing(LearningHistory.WeakArea::getErrorCount).reversed())
            .limit(2)
            .toList();
    for (LearningHistory.WeakArea area : sorted) {
      LessonServiceClient.RevisionExercise exercise =
          lessonServiceClient.findRevisionExercise(
              language, area.getCategory(), area.getSubcategory());
      if (exercise == null) {
        continue;
      }
      Recommendation rec =
          Recommendation.builder()
              .userId(userId)
              .targetLanguageCode(language)
              .type(Recommendation.RecommendationType.revision)
              .targetId(exercise.id())
              .targetType(Recommendation.TargetType.exercise)
              .title("Révision: " + area.getSubcategory())
              .reason("Vous avez fait %d erreurs sur ce point".formatted(area.getErrorCount()))
              .priority(2)
              .metadata(
                  Map.of(
                      "estimatedMinutes",
                      Optional.ofNullable(exercise.estimatedMinutes()).orElse(10),
                      "lastErrorAt",
                      area.getLastErrorAt()))
              .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
              .build();
      recs.add(rec);
    }
    return recs;
  }

  public List<Recommendation> generateConversation(
      String userId, String language, String level, UserPreferences preferences) {
    List<Recommendation> recs = new ArrayList<>();
    var slots = conversationServiceClient.getAvailableSlots(language, level);
    if (slots.isEmpty()) return recs;
    List<String> preferredTimes =
        preferences != null && preferences.getPreferredLearningTime() != null
            ? preferences.getPreferredLearningTime()
            : List.of();
    slots.stream()
        .filter(
            slot ->
                preferredTimes.isEmpty()
                    || preferredTimes.stream()
                        .anyMatch(
                            pref ->
                                slot.startTime()
                                    .toString()
                                    .toLowerCase()
                                    .contains(pref.toLowerCase())))
        .sorted(Comparator.comparing(ConversationServiceClient.ConversationSlot::startTime))
        .limit(2)
        .forEach(
            slot -> {
              Recommendation rec =
                  Recommendation.builder()
                      .userId(userId)
                      .targetLanguageCode(language)
                      .type(Recommendation.RecommendationType.conversation)
                      .targetId(slot.id())
                      .targetType(Recommendation.TargetType.slot)
                      .title("Session conversation - %s".formatted(slot.startTime()))
                      .reason("Pratiquez avec d'autres apprenants")
                      .priority(3)
                      .metadata(
                          Map.of(
                              "startTime",
                              slot.startTime(),
                              "participantsCount",
                              slot.participantsCount(),
                              "maxParticipants",
                              slot.maxParticipants()))
                      .expiresAt(slot.startTime())
                      .build();
              recs.add(rec);
            });
    return recs;
  }

  public List<Recommendation> keepTop(List<Recommendation> list) {
    return list.stream()
        .sorted(
            Comparator.comparing(Recommendation::getPriority)
                .thenComparing(
                    Recommendation::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
        .limit(MAX_RECOMMENDATIONS)
        .toList();
  }
}
