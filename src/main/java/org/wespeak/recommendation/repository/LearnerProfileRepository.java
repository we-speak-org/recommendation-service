package org.wespeak.recommendation.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.wespeak.recommendation.model.entity.LearnerProfile;

public interface LearnerProfileRepository extends MongoRepository<LearnerProfile, String> {
  Optional<LearnerProfile> findByUserIdAndTargetLanguageCode(String userId, String targetLanguageCode);
}
