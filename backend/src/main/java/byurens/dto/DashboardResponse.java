package byurens.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
    List<HourlySalesResponse> hourlySales,
    List<TopItemResponse> topProducts,
    List<TopItemResponse> topAddOns,
    List<PaymentStatResponse> paymentStats,
    BigDecimal totalDailyRevenue,
    Long totalDailyOrders
) {}
