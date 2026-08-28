package byurens.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddOnRequest(
    @NotBlank
    String name,
    
    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal price,

    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal capital,

    boolean isAvailable
) {}
