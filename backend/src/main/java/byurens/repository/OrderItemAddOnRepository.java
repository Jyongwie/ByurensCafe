package byurens.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.OrderItemAddOn;

@Repository
public interface OrderItemAddOnRepository extends JpaRepository<OrderItemAddOn, UUID> {
    Optional<OrderItemAddOn> findByAddOnName(String addOnName);
}
