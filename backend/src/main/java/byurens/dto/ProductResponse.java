package byurens.dto;

import java.util.UUID;

import byurens.enums.ProductType;

public record ProductResponse(
    UUID id,
    String name,
    ProductType productType,
    String categoryName,
    boolean isAvailable
) {}
