package byurens.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CustomerResponse(
    UUID id,
    String name,
    String email,
    String phoneNumber,
    int loyaltyPoint,
    BigDecimal walletBalance 
) {}
