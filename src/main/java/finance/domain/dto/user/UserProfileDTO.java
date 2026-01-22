package finance.domain.dto.user;

import finance.domain.user.RoleUser;
import finance.domain.user.User;

public record UserProfileDTO(
        Long id,
        String name,
        String username,
        RoleUser role
) {
    public static UserProfileDTO toDTO(User user) {
        return new UserProfileDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole()
        );
    }
}
