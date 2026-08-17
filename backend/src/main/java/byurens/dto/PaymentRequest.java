package byurens.dto;

import java.math.BigDecimal;
import java.util.UUID;

import byurens.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
    @NotNull(message = "Order ID is required")
    UUID orderId,

    @NotNull
    @DecimalMin(value = "0.0", message = "Payment amount must be greater than zero")
    BigDecimal amount,

    @NotNull(message = "Payment method is required")
    PaymentMethod method
) {}
