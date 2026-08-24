package byurens.dto;

public record LoginResponse(
    String token,
    String role
) {}
