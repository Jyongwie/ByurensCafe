package byurens.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderItemResponse(
    UUID id,
    String productVariant,
    String productName,
    String sizeName,
    BigDecimal basePrice,
    int quantity,
    String note,
    BigDecimal totalPrice,
    List<OrderItemAddOnResponse> selectedAddOns
) {}
