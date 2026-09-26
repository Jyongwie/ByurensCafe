package byurens.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
    List<HourlySalesResponse> hourlySales,
    List<TopItemResponse> topProducts,
    List<TopItemResponse> topAddOns,
    List<StatDTO> paymentStats,
    List<StatDTO> orderTypes,
    List<StatDTO> loyaltyStats,
    List<StatDTO> categorySales,
    BigDecimal totalDailyRevenue,
    Long totalDailyOrders
) {}
