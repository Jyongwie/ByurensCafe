package byurens.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import byurens.dto.PaymentRequest;
import byurens.entities.Order;
import byurens.entities.Payment;
import byurens.enums.OrderStatus;
import byurens.enums.PaymentStatus;
import byurens.enums.TableStatus;
import byurens.exception.ByurensCafeException;
import byurens.repository.OrderRepository;
import byurens.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new ByurensCafeException("Order not found"));

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new ByurensCafeException("This order already been paid");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new ByurensCafeException("Cannot process payment cause the order already been cancelled");
        }

        Payment newPayment = Payment.builder()
            .order(order)
            .amount(request.amount())
            .method(request.method())
            .status(PaymentStatus.PAID)
            .build();
        
        paymentRepository.save(newPayment);

        List<Payment> successfulPayments = paymentRepository.findAllByOrderIdAndStatus(
            order.getId(),
            PaymentStatus.PAID
        );

        BigDecimal totalPaid = successfulPayments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(order.getTotalAmount()) >= 0) {
            order.setPaymentStatus(PaymentStatus.PAID);

            if (order.getTable() != null) {
                order.getTable().setStatus(TableStatus.AVAILABLE);
            }
        }

        return newPayment;
    }
}
