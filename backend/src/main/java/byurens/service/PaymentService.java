package byurens.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import byurens.entities.Order;
import byurens.enums.OrderStatus;
import byurens.enums.PaymentStatus;
import byurens.exception.ByurensCafeException;
import byurens.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentOrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public Order processPayment(UUID orderId, BigDecimal amountPaid) {
        Order paymentOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new ByurensCafeException("Order not found"));

        if (paymentOrder.getPaymentStatus() == PaymentStatus.PAID) {
            throw new ByurensCafeException("This order already been paid");
        }

        if (paymentOrder.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new ByurensCafeException("Cannot process payment cause the order already been cancelled");
        }

        if (amountPaid.compareTo(paymentOrder.getTotalAmount()) < 0) {
            throw new ByurensCafeException("Insufficient payment. The total amount is " + paymentOrder.getTotalAmount());
        }

        paymentOrder.setPaymentStatus(PaymentStatus.PAID);

        return orderRepository.save(paymentOrder);
    }
}
