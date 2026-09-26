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

import byurens.dto.HourlySalesResponse;
import byurens.dto.StatDTO;
import byurens.dto.TopItemResponse;
import byurens.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByOrderNumber(String orderNumber);

    @Query(
        "SELECT new byurens.dto.HourlySalesResponse(EXTRACT(HOUR FROM o.createdAt), SUM(o.totalAmount), COUNT(o.id)) " +
        "FROM Order o WHERE o.createdAt >= :start AND o.createdAt <= :end AND o.paymentStatus = 'PAID' " +
        "GROUP BY EXTRACT(HOUR FROM o.createdAt) ORDER BY EXTRACT(HOUR FROM o.createdAt)"
    )
    List<HourlySalesResponse> findHourlySales(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(
        "SELECT new byurens.dto.TopItemResponse(oi.productName, SUM(oi.quantity)) " +
        "FROM OrderItem oi JOIN oi.order o " +
        "WHERE o.createdAt >= :start AND o.createdAt <= :end AND o.paymentStatus = 'PAID' " +
        "GROUP BY oi.productName ORDER BY SUM(oi.quantity) DESC"
    )
    List<TopItemResponse> findTopProducts(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    @Query(
        "SELECT new byurens.dto.StatDTO(CAST(o.orderType AS string), COUNT(o.id)) " +
        "FROM Order o WHERE o.createdAt >= :start AND o.createdAt <= :end AND o.paymentStatus = 'PAID' " +
        "GROUP BY o.orderType"
    )
    List<StatDTO> findOrderTypeStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(
        "SELECT new byurens.dto.StatDTO(" +
        "CASE WHEN o.customer IS NULL THEN 'Walk-in' ELSE 'Registered Member' END, COUNT(o.id)) " +
        "FROM Order o WHERE o.createdAt >= :start AND o.createdAt <= :end AND o.paymentStatus = 'PAID' " +
        "GROUP BY CASE WHEN o.customer IS NULL THEN 'Walk-in' ELSE 'Registered Member' END"
    )
    List<StatDTO> findLoyaltyStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(
        "SELECT new byurens.dto.StatDTO(c.label, SUM(oi.quantity)) " +
        "FROM OrderItem oi JOIN oi.order o JOIN oi.variant v JOIN v.product p JOIN p.category c " +
        "WHERE o.createdAt >= :start AND o.createdAt <= :end AND o.paymentStatus = 'PAID' " +
        "GROUP BY c.label ORDER BY SUM(oi.quantity) DESC"
    )
    List<StatDTO> findCategorySales(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
