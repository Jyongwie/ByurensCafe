package byurens.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.HireStaffRequest;
import byurens.dto.StaffResponse;
import byurens.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/staffs")
@RequiredArgsConstructor 
public class StaffController {
    private final StaffService staffService;

    @PostMapping 
    public ResponseEntity<StaffResponse> hireNewStaff(@Valid @RequestBody HireStaffRequest request) {
        StaffResponse response = staffService.hireNewStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
