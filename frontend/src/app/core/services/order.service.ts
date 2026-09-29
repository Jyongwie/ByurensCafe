import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface OrderItemRequest {
    variantId: string;
    quantity: number;
    note?: string;
    addOnsId: string[];
}

export interface OrderRequest {
    customerId?: string | null;
    tableId?: string | null;
    orderType: "DINE_IN" | "TAKEAWAY";
    items: OrderItemRequest[];
}

@Injectable({providedIn: "root"})
export class OrderService {
    private http = inject(HttpClient);
    private apiUrl = "http://localhost:8080/api";

    submitOrder(request: OrderRequest): Observable<any> {
        return this.http.post(`${this.apiUrl}/orders`, request);
    }
}