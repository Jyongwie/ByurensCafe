package byurens.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import byurens.enums.OrderStatus;
import byurens.enums.OrderType;
import byurens.enums.PaymentStatus;

public record OrderResponse(
    UUID id,
    String orderNumber,
    String customerName,
    String tableIdentifier,
    OrderType orderType,
    OrderStatus orderStatus,
    PaymentStatus paymentStatus,
    BigDecimal totalAmount,
    LocalDateTime createdAt,
    List<OrderItemResponse> items
) {}
