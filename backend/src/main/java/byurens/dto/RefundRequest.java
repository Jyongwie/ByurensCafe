package byurens.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefundRequest(
    @NotNull(message = "Order Id is required")
    UUID orderId,

    @NotNull(message = "Refund amount id required")
    @DecimalMin(value = "0.0", message = "Refund amount must be greater than zero")
    BigDecimal refundAmount,

    @NotBlank(message = "Reason for the refund is required")
    String reason
) {}
