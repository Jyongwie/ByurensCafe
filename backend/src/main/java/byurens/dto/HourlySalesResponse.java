package byurens.dto;

import java.math.BigDecimal;

public record HourlySalesResponse(
    Integer hour,
    BigDecimal revenue,
    Long orderCount
) {}
