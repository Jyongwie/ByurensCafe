package byurens.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.HireStaffRequest;
import byurens.dto.StaffResponse;
import byurens.entities.Staff;
import byurens.entities.User;
import byurens.exception.ByurensCafeException;
import byurens.repository.StaffRepository;
import byurens.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StaffResponse hireNewStaff(HireStaffRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ByurensCafeException("Email already been registered");
        }

        User user = User.builder()
            .email(request.email())
            .phoneNumber(request.phoneNumber())
            .passwordHash(passwordEncoder.encode(request.rawPassword()))
            .build();

        User savedUser = userRepository.save(user);
        Staff staff = Staff.builder()
            .user(savedUser)
            .name(request.name())
            .role(request.role())
            .hourlyRate(request.hourlyRate())
            .hireDate(request.hireDate())
            .build();

        Staff savedStaff = staffRepository.save(staff);
        return mapToResponse(savedStaff);
    }

    private StaffResponse mapToResponse(Staff staff) {
        return new StaffResponse(
            staff.getId(),
            staff.getName(),
            staff.getRole(),
            staff.getHourlyRate(),
            staff.getHireDate()
        );
    }
}
