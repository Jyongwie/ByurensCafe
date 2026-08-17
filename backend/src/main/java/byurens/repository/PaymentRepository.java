package byurens.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.Payment;
import byurens.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findAllByOrderIdAndStatus(UUID orderId, PaymentStatus status);
}
