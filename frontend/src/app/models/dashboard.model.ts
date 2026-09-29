export interface DashboardResponse {
    hourlySales: {hour: number; revenue: number; orderCount: number}[];
    topProducts: {name: string; quantity: number}[];
    topAddOns: {name: string; quantity: number}[];
    paymentStats: {method: string; count: number}[];
    orderTypes: {type: string; count: number}[];
    loyaltyStats: {type: string; count: number}[];
    categorySales: {category: string; quantity: number}[];
    totalDailyRevenue: number;
    totalDailyOrders: number;
}
