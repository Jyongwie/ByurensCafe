package byurens.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import byurens.dto.LoginRequest;
import byurens.dto.LoginResponse;
import byurens.entities.Staff;
import byurens.entities.User;
import byurens.exception.ByurensCafeException;
import byurens.repository.StaffRepository;
import byurens.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new ByurensCafeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ByurensCafeException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new ByurensCafeException("This account has been disable");
        }

        String role = "CUSTOMER";
        Optional<Staff> staff = staffRepository.findById(user.getId());

        if (staff.isPresent()) {
            role = staff.get().getRole().name();
        }

        String token = jwtService.generateToken(user, role);
        return new LoginResponse(token, role);
    }
}
