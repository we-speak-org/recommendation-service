package org.wespeak.recommendation.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.wespeak.recommendation.model.entity.RecommendationCandidate;

public interface RecommendationCandidateRepository
    extends MongoRepository<RecommendationCandidate, String> {
  List<RecommendationCandidate> findByTargetLanguageCodeAndLevel(
      String targetLanguageCode, String level);
}
