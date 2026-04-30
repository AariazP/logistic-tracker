import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthCredentials, AuthResponse } from '../../shared/models';
import { AuthStore } from './auth.store';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly authStore = inject(AuthStore);
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  login(credentials: AuthCredentials): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, credentials).pipe(
      tap((response) => {
        this.authStore.setAuth(response.token, {
          username: credentials.username,
          role: response.role,
        });
      }),
    );
  }

  logout(): void {
    this.authStore.clearAuth();
  }
}
