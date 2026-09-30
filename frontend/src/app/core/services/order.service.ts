import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { OrderRequest } from "../../models/order.model";

@Injectable({providedIn: "root"})
export class OrderService {
    private http = inject(HttpClient);
    private apiUrl = "http://localhost:8080/api";

    submitOrder(request: OrderRequest): Observable<any> {
        return this.http.post(`${this.apiUrl}/orders`, request);
    }
}