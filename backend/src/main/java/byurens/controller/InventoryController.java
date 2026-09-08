package byurens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.StockAdjustmentRequest;
import byurens.dto.StockAdjustmentResponse;
import byurens.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/inventory")
@RequiredArgsConstructor 
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/add-stock")
    public ResponseEntity<StockAdjustmentResponse> addStock(@Valid @RequestBody StockAdjustmentRequest request) {
        StockAdjustmentResponse response = inventoryService.addStock(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deduct-stock")
    public ResponseEntity<StockAdjustmentResponse> deductStock(@Valid @RequestBody StockAdjustmentRequest request) {
        StockAdjustmentResponse response = inventoryService.deductStock(request);
        return ResponseEntity.ok(response);
    }
}
