package byurens.dto;

import java.util.UUID;

import byurens.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
    @NotBlank(message = "Product name is required")
    String name,

    @NotNull(message = "Product type is required")
    ProductType productType,

    @NotNull(message = "Category ID is required")
    UUID categoryId,
    
    boolean isAvailable
) {}
