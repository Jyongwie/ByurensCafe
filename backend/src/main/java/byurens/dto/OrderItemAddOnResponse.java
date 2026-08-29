package byurens.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemAddOnResponse(
    UUID id,
    String addOnName,
    BigDecimal priceCharged
) {}
