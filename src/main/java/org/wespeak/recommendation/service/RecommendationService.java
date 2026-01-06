package org.wespeak.recommendation.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wespeak.recommendation.dto.*;
import org.wespeak.recommendation.model.entity.RecommendationLog;
import org.wespeak.recommendation.repository.RecommendationLogRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

  private final RecommendationLogRepository recommendationLogRepository;

  public NextLessonResponse getNextLesson(String targetLanguageCode, String context) {
    LessonRecommendationDto recommendation =
        LessonRecommendationDto.builder()
            .lessonId("lesson-past-continuous")
            .titleKey("lesson.past_continuous.title")
            .type("grammar")
            .level("B1")
            .estimatedDuration(15)
            .score(0.92)
            .reason("SKILL_GAP")
            .reasonKey("recommendation.reason.skill_gap")
            .targetedSkills(
                List.of(
                    TargetedSkillDto.builder()
                        .skillId("skill-past-continuous")
                        .skillCode("grammar.past_continuous")
                        .currentMastery(45)
                        .build()))
            .build();

    AlternativeRecommendationDto alternative =
        AlternativeRecommendationDto.builder()
            .lessonId("lesson-vocabulary-business")
            .titleKey("lesson.vocabulary.business")
            .score(0.87)
            .reason("POPULAR")
            .build();

    ContextInfoDto contextInfo =
        ContextInfoDto.builder()
            .totalSkillGaps(5)
            .daysSinceLastLesson(2)
            .recommendationGenerated(Instant.now().toString())
            .build();

    log.debug("Generated next lesson for lang={} context={}", targetLanguageCode, context);
    return NextLessonResponse.builder()
        .recommendation(recommendation)
        .alternatives(List.of(alternative))
        .contextInfo(contextInfo)
        .build();
  }

  public List<LessonRecommendationDto> getRecommendedLessons(String targetLanguageCode) {
    return List.of(
        LessonRecommendationDto.builder()
            .lessonId("lesson-past-continuous")
            .titleKey("lesson.past_continuous.title")
            .type("grammar")
            .level("B1")
            .estimatedDuration(15)
            .score(0.92)
            .reason("SKILL_GAP")
            .reasonKey("recommendation.reason.skill_gap")
            .build(),
        LessonRecommendationDto.builder()
            .lessonId("lesson-food-vocabulary")
            .titleKey("lesson.food_vocabulary")
            .type("vocab")
            .level("B1")
            .estimatedDuration(12)
            .score(0.85)
            .reason("POPULAR")
            .reasonKey("recommendation.reason.popular")
            .build());
  }

  public List<ConversationTopicRecommendationDto> getConversationTopics(String targetLanguageCode) {
    return List.of(
        ConversationTopicRecommendationDto.builder()
            .topicCode("restaurant.ordering")
            .titleKey("topic.restaurant.ordering")
            .level("B1")
            .estimatedDuration(15)
            .score(0.88)
            .build(),
        ConversationTopicRecommendationDto.builder()
            .topicCode("business_meetings")
            .titleKey("topic.business_meetings")
            .level("B2")
            .estimatedDuration(20)
            .score(0.82)
            .build());
  }

  public NextActionResponse getNextAction(String targetLanguageCode) {
    NextActionResponse.ReasoningDto reasoning =
        NextActionResponse.ReasoningDto.builder()
            .primary("balance_learning_practice")
            .factors(Map.of("consecutiveLessons", 4, "timeOfDay", "evening", "skillReadiness", 0.78))
            .build();

    NextActionResponse.NextActionRecommendationDto recommendation =
        NextActionResponse.NextActionRecommendationDto.builder()
            .type("conversation")
            .topicCode("restaurant.ordering")
            .estimatedDuration(15)
            .titleKey("topic.restaurant.ordering")
            .build();

    NextActionResponse.NextActionRecommendationDto alternative =
        NextActionResponse.NextActionRecommendationDto.builder()
            .type("lesson")
            .lessonId("lesson-food_vocabulary")
            .titleKey("lesson.food_vocabulary")
            .estimatedDuration(12)
            .build();

    return NextActionResponse.builder()
        .action("CONVERSATION")
        .confidence(0.85)
        .reasoning(reasoning)
        .recommendation(recommendation)
        .alternativeAction(alternative)
        .build();
  }

  public LearningPathResponse getLearningPath(String targetLanguageCode) {
    LearningPathResponse.MilestoneDto milestone =
        LearningPathResponse.MilestoneDto.builder()
            .milestone("Complete B1 Grammar")
            .lessonsRemaining(8)
            .estimatedDays(12)
            .build();

    LearningPathResponse.PlannedActivityDto activityLesson =
        LearningPathResponse.PlannedActivityDto.builder()
            .day("Monday")
            .type("LESSON")
            .itemId("lesson-grammar-b1")
            .duration(15)
            .build();

    LearningPathResponse.PlannedActivityDto activityConversation =
        LearningPathResponse.PlannedActivityDto.builder()
            .day("Wednesday")
            .type("CONVERSATION")
            .topicCode("daily_life.hobbies")
            .duration(15)
            .build();

    LearningPathResponse.WeeklyPlanDto weeklyPlan =
        LearningPathResponse.WeeklyPlanDto.builder()
            .week(1)
            .activities(List.of(activityLesson, activityConversation))
            .build();

    LearningPathResponse.PredictedOutcomeDto predictedOutcome =
        LearningPathResponse.PredictedOutcomeDto.builder()
            .estimatedLevelIn30Days("B1+")
            .estimatedXPGain(2500)
            .skillsToMaster(List.of("grammar.past_tenses", "vocabulary.work"))
            .build();

    return LearningPathResponse.builder()
        .currentPhase("skill_building")
        .phaseProgress(0.6)
        .upcomingMilestones(List.of(milestone))
        .recommendedPath(List.of(weeklyPlan))
        .predictedOutcome(predictedOutcome)
        .build();
  }

  public SkillGapsResponse getSkillGaps(String targetLanguageCode) {
    SkillGapsResponse.GapEvidenceDto evidence =
        SkillGapsResponse.GapEvidenceDto.builder()
            .errorCount(8)
            .lastError(Instant.now().toString())
            .relatedLessonsNotCompleted(2)
            .build();

    SkillGapsResponse.RecommendedActionDto action =
        SkillGapsResponse.RecommendedActionDto.builder()
            .type("LESSON")
            .itemId("lesson.past_continuous_intro")
            .titleKey("lesson.past_continuous_intro")
            .build();

    SkillGapsResponse.IdentifiedGapDto gap =
        SkillGapsResponse.IdentifiedGapDto.builder()
            .skillId("skill-past-continuous")
            .skillCode("grammar.past_continuous")
            .nameKey("skill.grammar.past_continuous")
            .category("grammar")
            .priority(1)
            .evidence(evidence)
            .recommendedActions(List.of(action))
            .build();

    SkillGapsResponse.StrengthAreaDto strength =
        SkillGapsResponse.StrengthAreaDto.builder()
            .skillCode("vocabulary.daily_life")
            .masteryLevel(92)
            .lastPracticed(Instant.now().toString())
            .build();

    SkillGapsResponse.SummaryDto summary =
        SkillGapsResponse.SummaryDto.builder()
            .totalGaps(5)
            .highPriorityGaps(2)
            .strengthsCount(12)
            .overallReadiness(0.75)
            .build();

    return SkillGapsResponse.builder()
        .identifiedGaps(List.of(gap))
        .strengthAreas(List.of(strength))
        .summary(summary)
        .build();
  }

  public FeedbackResponse recordFeedback(FeedbackRequest request) {
    RecommendationLog log =
        RecommendationLog.builder()
            .userId("unknown") // user extraction to be added when security context is wired
            .targetLanguageCode("unknown")
            .recommendationType("feedback")
            .userAction(request.getAction())
            .actionTimestamp(Instant.parse(request.getTimestamp()))
            .build();
    recommendationLogRepository.save(log);
    return new FeedbackResponse(true, "Feedback recorded for model improvement");
  }
}
