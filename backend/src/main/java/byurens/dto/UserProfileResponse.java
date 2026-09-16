package byurens.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UserProfileResponse(
    UUID id,
    String email,
    String name,
    String role,
    String phoneNumber,
    int loyaltyPoint,
    BigDecimal walletBalace
) {}
