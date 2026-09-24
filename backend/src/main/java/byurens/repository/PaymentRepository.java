package byurens.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import byurens.dto.PaymentStatResponse;
import byurens.entities.Payment;
import byurens.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findAllByOrderIdAndStatus(UUID orderId, PaymentStatus status);

    @Query(
        "SELECT new byurens.dto.PaymentStatResponse(CAST(p.method AS string), COUNT(p.id)) " +
        "FROM Payment p " +
        "WHERE p.createdAt >= :start AND p.createdAt <= :end AND p.status = 'PAID' " +
        "GROUP BY p.method"
    )
    List<PaymentStatResponse> findPaymentStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
