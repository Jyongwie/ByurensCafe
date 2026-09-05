package byurens.dto;

import byurens.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(
    @NotNull(message = "Order status is required")
    OrderStatus status
) {}
