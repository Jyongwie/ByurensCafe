package byurens.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import byurens.dto.DashboardResponse;
import byurens.dto.HourlySalesResponse;
import byurens.dto.StatDTO;
import byurens.dto.TopItemResponse;
import byurens.repository.OrderItemAddOnRepository;
import byurens.repository.OrderRepository;
import byurens.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class DashboardService {
    private final OrderRepository orderRepository;
    private final OrderItemAddOnRepository orderItemAddOnRepository;
    private final PaymentRepository paymentRepository;

    public DashboardResponse getTodayDashboardData() {
        LocalDateTime startDay = LocalDate.now().atStartOfDay();
        LocalDateTime endDay = startDay.plusDays(1).minusNanos(1);
        
        List<HourlySalesResponse> hourlySales = orderRepository.findHourlySales(startDay, endDay);
        List<TopItemResponse> topProducts = orderRepository.findTopProducts(startDay, endDay, PageRequest.of(0, 5));
        List<TopItemResponse> topAddOns = orderItemAddOnRepository.findTopAddOns(startDay, endDay, PageRequest.of(0, 5));
        List<StatDTO> paymentStats = paymentRepository.findPaymentStats(startDay, endDay);
        List<StatDTO> orderTypes = orderRepository.findOrderTypeStats(startDay, endDay);
        List<StatDTO> loyaltyStats = orderRepository.findLoyaltyStats(startDay, endDay);
        List<StatDTO> categorySales = orderRepository.findCategorySales(startDay, endDay);

        BigDecimal totalRevenue = hourlySales.stream()
            .map(HourlySalesResponse::revenue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalOrders = hourlySales.stream()
            .mapToLong(HourlySalesResponse::orderCount)
            .sum();

        return new DashboardResponse(
            hourlySales,
            topProducts,
            topAddOns,
            paymentStats,
            orderTypes,
            loyaltyStats,
            categorySales,
            totalRevenue,
            totalOrders
        );
    }
}
