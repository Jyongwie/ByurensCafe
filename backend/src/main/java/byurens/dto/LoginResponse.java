package byurens.dto;

import byurens.enums.Role;

public record LoginResponse(
    String token,
    Role role
) {}
