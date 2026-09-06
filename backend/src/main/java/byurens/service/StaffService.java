package byurens.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.HireStaffRequest;
import byurens.dto.StaffResponse;
import byurens.entities.Staff;
import byurens.entities.User;
import byurens.repository.StaffRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final UserService userService;

    @Transactional
    public StaffResponse hireNewStaff(HireStaffRequest request) {
        User savedUser = userService.createUserAccount(
            request.email(),
            request.phoneNumber(),
            request.rawPassword()
        );

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
