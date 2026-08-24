package byurens.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.InventoryItem;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {}
