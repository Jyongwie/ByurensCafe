package byurens.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import byurens.enums.PaymentMethod;
import byurens.enums.PaymentStatus;

public record PaymentResponse(
    UUID id,
    UUID orderId,
    BigDecimal amount,
    PaymentMethod method,
    String transactionReference,
    PaymentStatus status,
    LocalDateTime createdAt
) {}
