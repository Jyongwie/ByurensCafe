package byurens.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.entities.User;
import byurens.exception.ByurensCafeException;
import byurens.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional 
    public User createUserAccount(String email, String phoneNumber, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new ByurensCafeException("Email already been registered");
        }

        User user = User.builder()
            .email(email)
            .phoneNumber(phoneNumber)
            .passwordHash(passwordEncoder.encode(rawPassword))
            .build();

        return userRepository.save(user);
    }
}
