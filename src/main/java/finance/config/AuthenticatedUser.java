package finance.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthenticatedUser {

    public static Long getAuthenticatedUserId() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    
          if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }
        return (Long) auth.getPrincipal();
    }
}
