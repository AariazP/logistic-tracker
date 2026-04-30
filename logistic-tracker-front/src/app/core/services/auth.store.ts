import { Injectable, signal, computed } from '@angular/core';
import { User } from '../../shared/models';

@Injectable({ providedIn: 'root' })
export class AuthStore {
  private readonly _token = signal<string | null>(this.loadToken());
  private readonly _user = signal<User | null>(this.loadUser());

  readonly token = computed(() => this._token());
  readonly user = computed(() => this._user());
  readonly isAuthenticated = computed(() => !!this._token());
  readonly isAdmin = computed(() => this._user()?.role === 'ADMIN');
  readonly isDriver = computed(() => this._user()?.role === 'DRIVER');

  private loadToken(): string | null {
    return sessionStorage.getItem('auth_token');
  }

  private loadUser(): User | null {
    const raw = sessionStorage.getItem('auth_user');
    if (!raw) return null;
    try {
      return JSON.parse(raw) as User;
    } catch {
      return null;
    }
  }

  setAuth(token: string, user: User): void {
    this._token.set(token);
    this._user.set(user);
    sessionStorage.setItem('auth_token', token);
    sessionStorage.setItem('auth_user', JSON.stringify(user));
  }

  clearAuth(): void {
    this._token.set(null);
    this._user.set(null);
    sessionStorage.removeItem('auth_token');
    sessionStorage.removeItem('auth_user');
  }
}
