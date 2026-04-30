export type UserRole = 'ADMIN' | 'DRIVER';

export interface User {
  username: string;
  role: UserRole;
}

export interface AuthCredentials {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  role: UserRole;
}
