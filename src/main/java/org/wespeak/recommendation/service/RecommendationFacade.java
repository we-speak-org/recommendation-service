package org.wespeak.recommendation.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wespeak.recommendation.dto.LearningHistoryDto;
import org.wespeak.recommendation.dto.RecommendationItemDto;
import org.wespeak.recommendation.dto.RecommendationsResponse;
import org.wespeak.recommendation.dto.UpdatePreferencesRequest;
import org.wespeak.recommendation.dto.UserPreferencesDto;
import org.wespeak.recommendation.messaging.RecommendationEventPublisher;
import org.wespeak.recommendation.model.entity.LearningHistory;
import org.wespeak.recommendation.model.entity.Recommendation;
import org.wespeak.recommendation.model.entity.UserPreferences;
import org.wespeak.recommendation.repository.LearningHistoryRepository;
import org.wespeak.recommendation.repository.RecommendationRepository;
import org.wespeak.recommendation.repository.UserPreferencesRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationFacade {

  private final RecommendationRepository recommendationRepository;
  private final UserPreferencesRepository userPreferencesRepository;
  private final LearningHistoryService learningHistoryService;
  private final LearningHistoryRepository learningHistoryRepository;
  private final RecommendationEngineService recommendationEngineService;
  private final RecommendationEventPublisher eventPublisher;

  @Transactional
  public RecommendationsResponse getRecommendations(
      String userId, String language, Integer limit, String type) {
    Instant now = Instant.now();
    // purge expired
    List<Recommendation> existing = recommendationRepository.findActive(userId, language, now);
    List<Recommendation> filtered =
        existing.stream()
            .filter(r -> type == null || r.getType().name().equals(type))
            .toList();
    if (filtered.size() >= limit) {
      return response(filtered, now);
    }

    LearningHistory history = learningHistoryService.getOrCreate(userId, language);
    UserPreferences prefs = userPreferencesRepository.findByUserIdAndTargetLanguageCode(userId, language).orElse(null);

    List<Recommendation> generated = new ArrayList<>(existing);
    if (type == null || type.equals("next_lesson")) {
      generated.addAll(recommendationEngineService.generateNextLesson(userId, language, history));
    }
    if (type == null || type.equals("revision")) {
      generated.addAll(recommendationEngineService.generateRevision(userId, language, history));
    }
    if (type == null || type.equals("conversation")) {
      generated.addAll(
          recommendationEngineService.generateConversation(
              userId, language, null, prefs));
    }

    List<Recommendation> saved =
        recommendationRepository.saveAll(recommendationEngineService.keepTop(generated));
    eventPublisher.publishGenerated(userId, language, saved);
    return response(saved, now);
  }

  @Transactional
  public RecommendationItemDto click(String userId, String recommendationId) {
    Recommendation rec =
        recommendationRepository
            .findByIdAndUserId(recommendationId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Recommandation introuvable"));
    rec.setClickedAt(Instant.now());
    recommendationRepository.save(rec);
    eventPublisher.publishClicked(rec);
    return toDto(rec);
  }

  @Transactional
  public RecommendationsResponse dismiss(String userId, String recommendationId) {
    Recommendation rec =
        recommendationRepository
            .findByIdAndUserId(recommendationId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Recommandation introuvable"));
    rec.setDismissed(true);
    recommendationRepository.save(rec);
    return response(
        recommendationRepository.findActive(userId, rec.getTargetLanguageCode(), Instant.now()),
        Instant.now());
  }

  @Transactional(readOnly = true)
  public UserPreferencesDto getPreferences(String userId, String language) {
    return userPreferencesRepository
        .findByUserIdAndTargetLanguageCode(userId, language)
        .map(this::toDto)
        .orElseGet(() -> toDto(UserPreferences.builder().userId(userId).targetLanguageCode(language).dailyGoalMinutes(15).build()));
  }

  @Transactional
  public UserPreferencesDto updatePreferences(
      String userId, String language, UpdatePreferencesRequest request) {
    UserPreferences prefs =
        userPreferencesRepository
            .findByUserIdAndTargetLanguageCode(userId, language)
            .orElseGet(
                () ->
                    UserPreferences.builder()
                        .userId(userId)
                        .targetLanguageCode(language)
                        .dailyGoalMinutes(15)
                        .build());
    if (request.getPreferredLearningTime() != null) {
      prefs.setPreferredLearningTime(request.getPreferredLearningTime());
    }
    if (request.getDailyGoalMinutes() != null) {
      prefs.setDailyGoalMinutes(request.getDailyGoalMinutes());
    }
    if (request.getFocusAreas() != null) {
      prefs.setFocusAreas(request.getFocusAreas());
    }
    if (request.getExcludedTopics() != null) {
      prefs.setExcludedTopics(request.getExcludedTopics());
    }
    prefs = userPreferencesRepository.save(prefs);
    return toDto(prefs);
  }

  @Transactional(readOnly = true)
  public LearningHistoryDto getLearningHistory(String userId, String language) {
    LearningHistory history = learningHistoryService.getOrCreate(userId, language);
    return LearningHistoryDto.builder()
        .userId(userId)
        .targetLanguageCode(language)
        .summary(
            LearningHistoryDto.Summary.builder()
                .totalLessons(Optional.ofNullable(history.getTotalLessons()).orElse(0))
                .averageScore(Optional.ofNullable(history.getAverageScore()).orElse(0.0))
                .totalConversationMinutes(Optional.ofNullable(history.getTotalConversationMinutes()).orElse(0))
                .build())
        .weakAreas(
            history.getWeakAreas() == null
                ? List.of()
                : history.getWeakAreas().stream()
                    .map(
                        w ->
                            LearningHistoryDto.WeakAreaDto.builder()
                                .category(w.getCategory())
                                .subcategory(w.getSubcategory())
                                .errorCount(w.getErrorCount())
                                .lastErrorAt(w.getLastErrorAt())
                                .build())
                    .toList())
        .strongAreas(
            history.getStrongAreas() == null
                ? List.of()
                : history.getStrongAreas().stream()
                    .map(
                        s ->
                            LearningHistoryDto.StrongAreaDto.builder()
                                .category(s.getCategory())
                                .subcategory(s.getSubcategory())
                                .successRate(s.getSuccessRate())
                                .build())
                    .toList())
        .recentProgress(LearningHistoryDto.RecentProgress.builder().lessonsLastWeek(0).xpLastWeek(0).streak(0).build())
        .build();
  }

  public void recomputeForUser(String userId, String language) {
    if (language == null) return;
    getRecommendations(userId, language, 5, null);
  }

  private RecommendationsResponse response(List<Recommendation> recs, Instant generatedAt) {
    List<RecommendationItemDto> items =
        recs.stream().map(this::toDto).toList();
    return RecommendationsResponse.builder().recommendations(items).generatedAt(generatedAt).build();
  }

  private RecommendationItemDto toDto(Recommendation rec) {
    return RecommendationItemDto.builder()
        .id(rec.getId())
        .type(rec.getType().name())
        .targetId(rec.getTargetId())
        .targetType(rec.getTargetType().name())
        .title(rec.getTitle())
        .reason(rec.getReason())
        .priority(rec.getPriority())
        .metadata(rec.getMetadata())
        .expiresAt(rec.getExpiresAt())
        .clickedAt(rec.getClickedAt())
        .build();
  }

  private UserPreferencesDto toDto(UserPreferences prefs) {
    return UserPreferencesDto.builder()
        .id(prefs.getId())
        .userId(prefs.getUserId())
        .targetLanguageCode(prefs.getTargetLanguageCode())
        .preferredLearningTime(prefs.getPreferredLearningTime())
        .dailyGoalMinutes(prefs.getDailyGoalMinutes())
        .focusAreas(prefs.getFocusAreas())
        .excludedTopics(prefs.getExcludedTopics())
        .updatedAt(prefs.getUpdatedAt())
        .build();
  }
}
