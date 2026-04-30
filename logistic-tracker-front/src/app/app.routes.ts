import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/pages/login-page.component').then((m) => m.LoginPageComponent),
  },
  {
    path: 'board',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/packages/pages/board-page.component').then((m) => m.BoardPageComponent),
  },
  { path: '', redirectTo: 'board', pathMatch: 'full' },
  { path: '**', redirectTo: 'board' },
];
