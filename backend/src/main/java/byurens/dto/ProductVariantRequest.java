package byurens.dto;

import java.math.BigDecimal;
import java.util.List;

import byurens.enums.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductVariantRequest(
    @NotNull
    Size size,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal price,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal onSale,

    @NotNull
    @Min(0)
    int onSalePercent,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal capital,

    boolean isPromo,

    List<RecipeIngredientRequest> recipeIngredients
) {}
