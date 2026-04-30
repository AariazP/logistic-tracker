import { UserRole } from './auth.model';

export interface Driver {
  id: string;
  username: string;
  role: UserRole;
  createdAt: string;
}

export interface CreateDriverRequest {
  username: string;
  password: string;
}
