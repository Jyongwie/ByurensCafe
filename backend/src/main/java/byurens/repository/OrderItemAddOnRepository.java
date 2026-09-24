package byurens.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import byurens.dto.TopItemResponse;
import byurens.entities.OrderItemAddOn;

@Repository
public interface OrderItemAddOnRepository extends JpaRepository<OrderItemAddOn, UUID> {
    Optional<OrderItemAddOn> findByAddOnName(String addOnName);

    @Query(
        "SELECT new byurens.dto.TopItemResponse(a.addOnName, SUM(oi.quantity)) " +
        "WHERE o.createdAt >= :start AND o.createdAt <= :end AND o.paymentStatus = 'PAID' " +
        "GROUP BY a.addOnName ORDER BY SUM(oi.quantity) DESC"
    )
    List<TopItemResponse> findTopAddOns(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);
}
