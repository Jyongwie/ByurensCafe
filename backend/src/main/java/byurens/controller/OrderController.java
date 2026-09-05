package byurens.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.OrderRequest;
import byurens.dto.OrderResponse;
import byurens.dto.OrderStatusRequest;
import byurens.enums.OrderStatus;
import byurens.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable UUID id, @Valid @RequestBody OrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(id, request.status());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable UUID id, @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.updateOrder(id, request);
        return ResponseEntity.ok(response);
    }
}
