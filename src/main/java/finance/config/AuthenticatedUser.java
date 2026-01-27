package finance.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import finance.exceptions.AuthenticationException;

public class AuthenticatedUser {

    public static Long getAuthenticatedUserId() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

          if (auth == null || !auth.isAuthenticated()) {
            throw AuthenticationException.userNotAuthenticated();
        }
        return (Long) auth.getPrincipal();
    }
}
