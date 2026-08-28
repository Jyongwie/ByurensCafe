package byurens.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record RecipeIngredientRequest(
    @NotNull
    UUID inventoryItemId,

    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal quantityRequired
) {}
