package org.wespeak.recommendation.util;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class UserContext {
  private UserContext() {}

  public static String userId(Authentication authentication) {
    if (authentication instanceof JwtAuthenticationToken token) {
      return token.getToken().getSubject();
    }
    if (authentication != null && authentication.getPrincipal() instanceof Map<?, ?> map) {
      Object sub = map.get("sub");
      if (sub != null) return sub.toString();
    }
    throw new IllegalStateException("User not authenticated");
  }
}
