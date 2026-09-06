package byurens.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.entities.User;
import byurens.exception.ByurensCafeException;
import byurens.repository.StaffRepository;
import byurens.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
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

    @Override 
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        staffRepository.findById(user.getId()).ifPresentOrElse(
            staff -> authorities.add(new SimpleGrantedAuthority("ROLE_" + staff.getRole().name())),
            () -> authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPasswordHash(),
            user.isActive(),
            true,
            true,
            true,
            authorities
        );
    }
}
