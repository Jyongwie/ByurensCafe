package byurens.dto;

import java.util.UUID;

public record UserProfileResponse(
    UUID id,
    String email,
    String name,
    String role,
    String phoneNumber
) {}
