package org.wespeak.recommendation.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.wespeak.recommendation.model.entity.UserPreferences;

public interface UserPreferencesRepository extends MongoRepository<UserPreferences, String> {
  Optional<UserPreferences> findByUserIdAndTargetLanguageCode(
      String userId, String targetLanguageCode);
}
