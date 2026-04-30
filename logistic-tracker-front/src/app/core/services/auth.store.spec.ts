import { describe, it, expect, beforeEach } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { AuthStore } from './auth.store';

describe('AuthStore', () => {
  let store: AuthStore;

  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({});
    store = TestBed.inject(AuthStore);
  });

  it('should start unauthenticated', () => {
    expect(store.isAuthenticated()).toBe(false);
    expect(store.user()).toBeNull();
  });

  it('should set auth state on login', () => {
    store.setAuth('mytoken', { username: 'admin', role: 'ADMIN' });
    expect(store.isAuthenticated()).toBe(true);
    expect(store.isAdmin()).toBe(true);
    expect(store.isDriver()).toBe(false);
    expect(store.token()).toBe('mytoken');
  });

  it('should detect DRIVER role', () => {
    store.setAuth('tok', { username: 'driver1', role: 'DRIVER' });
    expect(store.isDriver()).toBe(true);
    expect(store.isAdmin()).toBe(false);
  });

  it('should clear auth state on logout', () => {
    store.setAuth('tok', { username: 'admin', role: 'ADMIN' });
    store.clearAuth();
    expect(store.isAuthenticated()).toBe(false);
    expect(store.user()).toBeNull();
    expect(store.token()).toBeNull();
  });

  it('should persist token in sessionStorage', () => {
    store.setAuth('securetoken', { username: 'u', role: 'ADMIN' });
    expect(sessionStorage.getItem('auth_token')).toBe('securetoken');
  });

  it('should remove token from sessionStorage on clearAuth', () => {
    store.setAuth('tok', { username: 'u', role: 'ADMIN' });
    store.clearAuth();
    expect(sessionStorage.getItem('auth_token')).toBeNull();
  });
});
