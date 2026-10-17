import { HttpClient } from "@angular/common/http";
import { inject } from "@angular/core";
import { Observable } from "rxjs";
import { TableCafe, TableCafeRequest } from "../../models/venue.model";

export class VenueService {
    private http = inject(HttpClient);
    private apiUrl = "http://localhost:8080/api";

    getTables(): Observable<TableCafe[]> {
        return this.http.get<TableCafe[]>(`${this.apiUrl}/tables`);
    }

    createTable(request: TableCafeRequest): Observable<TableCafe> {
        return this.http.post<TableCafe>(this.apiUrl, request);
    }

    updateTable(id: string, request: TableCafeRequest): Observable<TableCafe> {
        return this.http.put<TableCafe>(`${this.apiUrl}/${id}`, request);
    }
}