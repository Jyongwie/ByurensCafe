package byurens.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.OrderItemAddOnResponse;
import byurens.dto.OrderItemRequest;
import byurens.dto.OrderItemResponse;
import byurens.dto.OrderRequest;
import byurens.dto.OrderResponse;
import byurens.entities.AddOn;
import byurens.entities.Customer;
import byurens.entities.Order;
import byurens.entities.OrderItem;
import byurens.entities.OrderItemAddOn;
import byurens.entities.ProductVariant;
import byurens.entities.TableCafe;
import byurens.enums.OrderStatus;
import byurens.enums.TableStatus;
import byurens.exception.ByurensCafeException;
import byurens.repository.AddOnRepository;
import byurens.repository.CustomerRepository;
import byurens.repository.OrderRepository;
import byurens.repository.ProductVariantRepository;
import byurens.repository.TableCafeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final TableCafeRepository tableCafeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AddOnRepository addOnRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
            .orderNumber(generateOrderNumber())
            .orderType(request.orderType())
            .build();

        if (request.customerId() != null) {
            Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ByurensCafeException("Customer not found"));
            order.setCustomer(customer);
        }

        if (request.tableId() != null) {
            TableCafe tableCafe = tableCafeRepository.findById(request.tableId())
                .orElseThrow(() -> new ByurensCafeException("Table not found"));
            
            if (tableCafe.getStatus() == TableStatus.OCCUPIED || tableCafe.getStatus() == TableStatus.RESERVED) {
                throw new  ByurensCafeException("Table " + tableCafe.getTableIdentifier() + " not available");
            }

            order.setTable(tableCafe);
            tableCafe.setStatus(TableStatus.OCCUPIED);
        }

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            ProductVariant productVariant = productVariantRepository.findById(itemRequest.variantId())
                .orElseThrow(() -> new ByurensCafeException("Variant not found"));

            OrderItem orderItem = OrderItem.builder()
                .order(order)
                .variant(productVariant)
                .productName(productVariant.getProduct().getName())
                .sizeName(productVariant.getSize().name())
                .basePrice(productVariant.getPrice())
                .quantity(itemRequest.quantity())
                .note(itemRequest.note())
                .build();

            BigDecimal addOnsTotal = BigDecimal.ZERO;

            if (itemRequest.addOnsId() != null) {
                for (UUID addOnsId : itemRequest.addOnsId()) {
                    AddOn addOn = addOnRepository.findById(addOnsId)
                        .orElseThrow(() -> new ByurensCafeException("Add-on not found"));
                    
                    OrderItemAddOn orderItemAddOn = OrderItemAddOn.builder()
                        .orderItem(orderItem)
                        .addOnName(addOn.getName())
                        .priceCharged(addOn.getPrice())
                        .build();

                    orderItem.getSelectedAddOns().add(orderItemAddOn);
                    addOnsTotal = addOnsTotal.add(addOn.getPrice());
                }
            }

            BigDecimal unitPrice = orderItem.getBasePrice().add(addOnsTotal);
            BigDecimal lineItemTotal = unitPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            orderItem.setTotalPrice(lineItemTotal);

            order.getOrderItems().add(orderItem);
            grandTotal = grandTotal.add(lineItemTotal);
        }

        order.setTotalAmount(grandTotal);

        Order savedOrder = orderRepository.save(order);
        
        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ByurensCafeException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new ByurensCafeException("Cannot change the order status that already " + order.getOrderStatus());
        }

        order.setOrderStatus(newStatus);

        if (newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED) {
            if (order.getTable() != null) {
                TableCafe tableCafe = order.getTable();
                tableCafe.setStatus(TableStatus.AVAILABLE);
            }
        }

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(UUID orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ByurensCafeException("Order not found"));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new ByurensCafeException("Only order with PENDING status that can be edit");
        }
        order.setOrderType(request.orderType());
        if (request.customerId() != null) {
            Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ByurensCafeException("Customer not found"));
            order.setCustomer(customer);
        } else {
            order.setCustomer(null);
        }

        if (order.getTable() != null && (request.tableId() == null || !order.getTable().getId().equals(request.tableId()))) {
            order.getTable().setStatus(TableStatus.AVAILABLE);
            order.setTable(null);
        }

        if (request.tableId() != null) {
            TableCafe newTable = tableCafeRepository.findById(request.tableId())
                .orElseThrow(() -> new ByurensCafeException("Table not found"));
            if ((newTable.getStatus() == TableStatus.OCCUPIED || newTable.getStatus() == TableStatus.RESERVED) && (order.getTable() == null || !order.getTable().getId().equals(newTable.getId()))) {
                throw new ByurensCafeException("Table " + newTable.getTableIdentifier() + " is not available");
            }
            newTable.setStatus(TableStatus.OCCUPIED);
            order.setTable(newTable);
        }
        order.getOrderItems().clear();
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            ProductVariant productVariant = productVariantRepository.findById(itemRequest.variantId())
                .orElseThrow(() -> new ByurensCafeException("Variant not found"));
            OrderItem orderItem = OrderItem.builder()
                .order(order)
                .variant(productVariant)
                .productName(productVariant.getProduct().getName())
                .sizeName(productVariant.getSize().name())
                .basePrice(productVariant.getPrice())
                .quantity(itemRequest.quantity())
                .note(itemRequest.note())
                .build();

            BigDecimal addOnsTotal = BigDecimal.ZERO;

            if (itemRequest.addOnsId() != null) {
                for (UUID addOnsId : itemRequest.addOnsId()) {
                    AddOn addOn = addOnRepository.findById(addOnsId)
                        .orElseThrow(() -> new ByurensCafeException("Add-on not found"));
                    OrderItemAddOn orderItemAddOn = OrderItemAddOn.builder()
                        .orderItem(orderItem)
                        .addOnName(addOn.getName())
                        .priceCharged(addOn.getPrice())
                        .build();

                    orderItem.getSelectedAddOns().add(orderItemAddOn);
                    addOnsTotal = addOnsTotal.add(addOn.getPrice());
                }
            }
            BigDecimal unitPrice = orderItem.getBasePrice().add(addOnsTotal);
            BigDecimal lineItemTotal = unitPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            orderItem.setTotalPrice(lineItemTotal);

            order.getOrderItems().add(orderItem);
            grandTotal = grandTotal.add(lineItemTotal);
        }
        order.setTotalAmount(grandTotal);
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    private String generateOrderNumber() {
        return "Byu-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    private OrderResponse mapToResponse(Order order) {
        String customerName = (order.getCustomer() != null)
            ? order.getCustomer().getName()
            : "Walk-in Customer";

        String tableIdentifier = (order.getTable() != null)
            ? order.getTable().getTableIdentifier()
            : "Takeaway";

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
            .map(item -> new OrderItemResponse(
                item.getId(),
                item.getVariant().getId().toString(),
                item.getProductName(),
                item.getSizeName(),
                item.getBasePrice(),
                item.getQuantity(),
                item.getNote(),
                item.getTotalPrice(),
                item.getSelectedAddOns().stream()
                    .map(addOn -> new OrderItemAddOnResponse(
                        addOn.getId(),
                        addOn.getAddOnName(),
                        addOn.getPriceCharged()
                    )).toList()
            )).toList();
        return new OrderResponse(
            order.getId(),
            order.getOrderNumber(),
            customerName,
            tableIdentifier,
            order.getOrderType(),
            order.getOrderStatus(),
            order.getPaymentStatus(),
            order.getTotalAmount(),
            order.getCreatedAt(),
            itemResponses
        );
    }
}
