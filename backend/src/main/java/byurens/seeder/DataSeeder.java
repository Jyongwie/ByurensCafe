package byurens.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import byurens.entities.Staff;
import byurens.entities.User;
import byurens.enums.Role;
import byurens.repository.StaffRepository;
import byurens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component 
@RequiredArgsConstructor 
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override 
    @Transactional 
    public void run(String... args) {
        String defaultEmail = "master@byurenscafe.com";

        if (userRepository.existsByEmail(defaultEmail)) {
            log.info("Master account already exists. Skipping database seed.");
            return;
        }

        log.info("Empty database detected. Creating default Master account...");

        User user = User.builder()
            .email(defaultEmail)
            .phoneNumber("081234567890")
            .passwordHash(passwordEncoder.encode("byurens#2026"))
            .build();

        User savedUser = userRepository.save(user);

        Staff staff = Staff.builder()
            .user(savedUser)
            .name("Master")
            .role(Role.OWNER)
            .build();

        staffRepository.save(staff);

        log.info("Default Master account created successfully");
        log.info("Email: {} | Password: {}", defaultEmail, "byurens#2026");
    }
}
