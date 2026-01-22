package finance.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import finance.config.AuthenticatedUser;
import finance.config.JWTService;
import finance.domain.dto.user.ResponseJwtDTO;
import finance.domain.dto.user.UserProfileDTO;
import finance.domain.dto.user.UserRegisterDTO;
import finance.domain.user.User;
import finance.exceptions.DuplicateResourceException;
import finance.exceptions.ResourceNotFoundException;
import finance.exceptions.UserNotFoundException;
import finance.repository.RepositoryUser;

@Service
public class ServiceAuth implements UserDetailsService {

    private final RepositoryUser userRepository;
    private final PasswordEncoder passwordEncoder;
   @Lazy private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public ServiceAuth(
            PasswordEncoder passwordEncoder,
            @Lazy AuthenticationManager authenticationManager,
            JWTService jwtService,
            RepositoryUser userRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return user;
    }

    public void register(UserRegisterDTO user) {
        User existingUser = userRepository.findByUsername(user.email());

        if (existingUser != null) {
            throw new DuplicateResourceException("Email", user.email());
        }

        String password = passwordEncoder.encode(user.password());
        User newUser = new User(user.name(), user.email(), password);
        userRepository.save(newUser);
    }

    public ResponseJwtDTO login(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                    password);

            authenticationManager.authenticate(authenticationToken);

            UserDetails userDetails = loadUserByUsername(email);
            String token = jwtService.createSecretKey((User) userDetails);

            return new ResponseJwtDTO(token);

        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new org.springframework.security.authentication.BadCredentialsException(
                    "Credenciais inválidas");
        }
    }

    public UserProfileDTO getUserProfile() {
        Long userId = AuthenticatedUser.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return UserProfileDTO.toDTO(user);
    }
}
