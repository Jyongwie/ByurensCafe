import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

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

@Injectable({providedIn:'root'})
export class DashboardService {
    private http = inject(HttpClient);

    getTodayDashboard(): Observable<DashboardResponse> {
        return this.http.get<DashboardResponse>('http://localhost:8080/api/dashboard/today');
    }
}