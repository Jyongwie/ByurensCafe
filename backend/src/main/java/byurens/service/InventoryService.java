package byurens.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.StockAdjustmentRequest;
import byurens.dto.StockAdjustmentResponse;
import byurens.entities.InventoryItem;
import byurens.exception.ByurensCafeException;
import byurens.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryItemRepository inventoryItemRepository;

    @Transactional
    public StockAdjustmentResponse addStock(StockAdjustmentRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.itemId())
            .orElseThrow(() -> new ByurensCafeException("Inventory item not found"));

        inventoryItem.setCurrentStock(inventoryItem.getCurrentStock().add(request.amount()));

        InventoryItem savedStock = inventoryItemRepository.save(inventoryItem);
        return mapToResponse(savedStock);
    }

    @Transactional
    public StockAdjustmentResponse deductStock(StockAdjustmentRequest request) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.itemId())
            .orElseThrow(() -> new ByurensCafeException("Inventory item not found"));

        if (inventoryItem.getCurrentStock().compareTo(request.amount()) < 0) {
            throw new ByurensCafeException(
                "Insufficient stock for " + inventoryItem.getName() + ".Amount: "
                + inventoryItem.getCurrentStock() + " " + inventoryItem.getUnitMeasurement() + " remaining"
            );
        }
        
        inventoryItem.setCurrentStock(inventoryItem.getCurrentStock().subtract(request.amount()));

        InventoryItem savedStock = inventoryItemRepository.save(inventoryItem);
        return mapToResponse(savedStock);
    }

    private StockAdjustmentResponse mapToResponse(InventoryItem inventoryItem) {
        return new StockAdjustmentResponse(
            inventoryItem.getId(),
            inventoryItem.getName(),
            inventoryItem.getUnitMeasurement(),
            inventoryItem.getCurrentStock(),
            inventoryItem.getLowStockThreshold()
        );
    }
}
