import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserService } from './user-service';
import { Observable, tap } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/v1/auth';

    constructor(private http: HttpClient, private userService: UserService) {}

    login(email: string, password: string): Observable<User> {
        return this.http.post<User>(`${this.apiUrl}/login`, { email, password }).pipe(
        tap(user => {
            localStorage.setItem('token', user.token!); // save JWT
            this.userService.setUser(user);             // update user state
        })
        );
    }

    register(data: { name: string; email: string; password: string }): Observable<User> {
        return this.http.post<User>(`${this.apiUrl}/register`, data).pipe(
        tap(user => {
            localStorage.setItem('token', user.token!);
            this.userService.setUser(user);
        })
        );
    }

    logout() {
        localStorage.removeItem('token');
        this.userService.setUser(null);
    }

    // Called when app starts (restore session)
    loadUserFromStorage() {
        const token = localStorage.getItem('token');
        if (token) {
        // optionally decode token to extract user info
        const user: User = { id: 123, name: 'demo', email: 'demo@test.com', token };
        this.userService.setUser(user);
        }
    }
}
