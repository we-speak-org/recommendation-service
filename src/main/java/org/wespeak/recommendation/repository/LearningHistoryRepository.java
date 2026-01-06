package org.wespeak.recommendation.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.wespeak.recommendation.model.entity.LearningHistory;

public interface LearningHistoryRepository extends MongoRepository<LearningHistory, String> {
  Optional<LearningHistory> findByUserIdAndTargetLanguageCode(
      String userId, String targetLanguageCode);
}
