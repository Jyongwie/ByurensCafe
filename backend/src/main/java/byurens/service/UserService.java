package byurens.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.UserProfileResponse;
import byurens.entities.Customer;
import byurens.entities.Staff;
import byurens.entities.User;
import byurens.exception.ByurensCafeException;
import byurens.repository.CustomerRepository;
import byurens.repository.StaffRepository;
import byurens.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
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

    public UserProfileResponse getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ByurensCafeException("User not found"));

        String displayName = "Guest";
        String displayRole = "GUEST";

        Optional<Staff> staff = staffRepository.findById(user.getId());
        if (staff.isPresent()) {
            displayName = staff.get().getName();
            displayRole = staff.get().getRole().name();
        } else {
            Optional<Customer> customer = customerRepository.findById(user.getId());
            if (customer.isPresent()) {
                displayName = customer.get().getName();
                displayRole = "CUSTOMER";
            }
        }

        return new UserProfileResponse(
            user.getId(),
            user.getEmail(),
            displayName,
            displayRole,
            user.getPhoneNumber()
        );
    }
}
