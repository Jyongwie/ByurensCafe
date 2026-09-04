package byurens.dto;

import java.math.BigDecimal;
import java.util.UUID;

import byurens.enums.UnitMeasurement;

public record StockAdjustmentResponse(
    UUID id,
    String name,
    UnitMeasurement unitMeasurement,
    BigDecimal currentStock,
    BigDecimal lowStockThreshold,
    boolean isLowStock
) {}
