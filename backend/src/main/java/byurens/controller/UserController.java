package byurens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.UserProfileResponse;
import byurens.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/users")
@RequiredArgsConstructor 
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        UserProfileResponse response = userService.getUserProfile();
        return ResponseEntity.ok(response);
    }
}
