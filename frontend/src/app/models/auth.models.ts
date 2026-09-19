export interface LoginCredentials {
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
}

export interface UserProfileResponse {
    id: string;
    email: string;
    name: string;
    role: string;
    phoneNumber: string;
}