package byurens.dto;

import java.util.List;
import java.util.UUID;

import byurens.enums.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    UUID customerId,
    UUID tableId,

    @NotNull(message = "Order type is required")
    OrderType orderType,

    @NotEmpty(message = "An order must contain at least one item")
    @Valid
    List<OrderItemRequest> items
) {}
