import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { AuthResponse, LoginCredentials, UserProfileResponse } from "../../models/auth.models";
import { catchError, Observable, tap } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private http = inject(HttpClient);
    private baseUrl = 'http://localhost:8080/api';

    currentUser = signal<UserProfileResponse | null>(null);

    login(credentials: LoginCredentials): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, credentials).pipe(
            tap((response) => {
                if (typeof window !== 'undefined') {
                    localStorage.setItem('jwt_token', response.token);
                }
                this.loadUserProfile().subscribe();
            })
        );
    }

    loadUserProfile(): Observable<UserProfileResponse> {
        return this.http.get<UserProfileResponse>(`${this.baseUrl}/users/me`).pipe(
            tap((profile) => {
                this.currentUser.set(profile);
            }),
            catchError((error) => {
                this.logout();
                throw error;
            })
        );
    }

    logout(): void {
        if (typeof window !== 'undefined') {
            localStorage.removeItem('jwt_token')
        }
        this.currentUser.set(null);
    }

    restoreSession(): void {
        if (typeof window !== 'undefined' && localStorage.getItem('jwt_token')) {
            this.loadUserProfile().subscribe();
        }
    }
}