import { HttpClient } from "@angular/common/http";
import { inject } from "@angular/core";
import { Observable } from "rxjs";
import { TableCafe } from "../../models/venue.model";

export class VenueService {
    private http = inject(HttpClient);
    private apiUrl = "http://localhost:8080/api";

    getTables(): Observable<TableCafe[]> {
        return this.http.get<TableCafe[]>(`${this.apiUrl}/tables`);
    }
}