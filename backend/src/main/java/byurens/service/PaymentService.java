package byurens.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.PaymentRequest;
import byurens.dto.PaymentResponse;
import byurens.dto.RefundRequest;
import byurens.entities.Order;
import byurens.entities.Payment;
import byurens.enums.OrderStatus;
import byurens.enums.PaymentMethod;
import byurens.enums.PaymentStatus;
import byurens.enums.TableStatus;
import byurens.exception.ByurensCafeException;
import byurens.repository.OrderRepository;
import byurens.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
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
        
        Payment savedPayment = paymentRepository.save(newPayment);

        List<Payment> successfulPayments = paymentRepository.findAllByOrderIdAndStatus(
            order.getId(),
            PaymentStatus.PAID
        );

        BigDecimal totalPaid = successfulPayments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(order.getTotalAmount()) >= 0) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }

        return mapToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse processRefund(RefundRequest request) {
        Order order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new ByurensCafeException("Order not found"));

        List<Payment> successfulPayments = paymentRepository.findAllByOrderIdAndStatus(
            order.getId(), 
            PaymentStatus.PAID
        );

        if (successfulPayments.isEmpty()) {
            throw new ByurensCafeException("No successful payments found to refund");
        }

        // BigDecimal totalPaid = BigDecimal.ZERO;

        // for (Payment payment : successfulPayments) {
        //     totalPaid = totalPaid.add(payment.getAmount());
        // }
        BigDecimal totalPaid = successfulPayments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (request.refundAmount().compareTo(totalPaid) > 0) {
            throw new ByurensCafeException("Cannot refund: " + request.refundAmount() + " . Only " + totalPaid + " has been paid");
        }

        PaymentMethod originalMethod = successfulPayments.get(0).getMethod();

        Payment refundRecord = Payment.builder()
            .order(order)
            .amount(request.refundAmount())
            .method(originalMethod)
            .status(PaymentStatus.REFUNDED)
            .build();

        Payment savedRefund = paymentRepository.save(refundRecord);

        if (request.refundAmount().compareTo(totalPaid) == 0) {
            order.setPaymentStatus(PaymentStatus.REFUNDED);
            order.setOrderStatus(OrderStatus.CANCELLED);

            if (order.getTable() != null) {
                order.getTable().setStatus(TableStatus.AVAILABLE);
            }
        }

        return mapToResponse(savedRefund);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getOrder().getId(),
            payment.getAmount(),
            payment.getMethod(),
            payment.getTransactionReference(),
            payment.getStatus(),
            payment.getCreatedAt()
        );
    }
}
