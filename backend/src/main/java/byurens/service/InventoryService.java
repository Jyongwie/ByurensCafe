package byurens.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.StockAdjustmentRequest;
import byurens.entities.InventoryItem;
import byurens.exception.ByurensCafeException;
import byurens.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryItemRepository inventoryItemRepository;

    @Transactional
    public InventoryItem addStock(StockAdjustmentRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.itemId())
            .orElseThrow(() -> new ByurensCafeException("Inventory item not found"));

        inventoryItem.setCurrentStock(inventoryItem.getCurrentStock().add(request.amount()));
        return inventoryItemRepository.save(inventoryItem);
    }

    @Transactional
    public InventoryItem deductStock(StockAdjustmentRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.itemId())
            .orElseThrow(() -> new ByurensCafeException("Inventory item not found"));

        if (inventoryItem.getCurrentStock().compareTo(request.amount()) < 0) {
            throw new ByurensCafeException(
                "Insufficient stock for " + inventoryItem.getName() + ".Amount: "
                + inventoryItem.getCurrentStock() + " " + inventoryItem.getUnitMeasurement() + " remaining"
            );
        }
        
        inventoryItem.setCurrentStock(inventoryItem.getCurrentStock().subtract(request.amount()));
        return inventoryItemRepository.save(inventoryItem);
    }
}
