export type UserRole = 'ADMIN' | 'DRIVER';

export interface User {
  id: string;
  username: string;
  role: UserRole;
}

export interface AuthCredentials {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}
