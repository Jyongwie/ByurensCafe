package byurens.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
    @NotNull(message = "Variant ID is required")
    UUID variantId,

    @Min(value = 1, message = "Quantity must be at least 1")
    int quantity,

    String note,

    List<UUID> addOnsId

) {
    public OrderItemRequest {
        if (addOnsId == null) {
            addOnsId = new ArrayList<>();
        }
    }
}
