package byurens.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.OrderRequest;
import byurens.dto.OrderResponse;
import byurens.dto.OrderStatusRequest;
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

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'BARISTA')")
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderResponse> response = orderService.getOrders(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }
}
