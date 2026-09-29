import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { DashboardResponse } from "../../models/dashboard.model";

@Injectable({providedIn:'root'})
export class DashboardService {
    private http = inject(HttpClient);

    getTodayDashboard(): Observable<DashboardResponse> {
        return this.http.get<DashboardResponse>('http://localhost:8080/api/dashboard/today');
    }
}