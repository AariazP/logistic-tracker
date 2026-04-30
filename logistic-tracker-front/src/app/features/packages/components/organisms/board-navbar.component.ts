import { Component, input, output } from '@angular/core';
import { User } from '../../../../shared/models';

@Component({
  selector: 'app-board-navbar',
  template: `
    <header class="navbar">
      <div class="navbar-brand">
        <span class="brand-icon">📦</span>
        <span>SmartShip</span>
      </div>
      <div class="navbar-right">
        @if (showAdminButton()) {
          <button class="btn-admin" (click)="openAdmin.emit()">Manage drivers and recipients</button>
        }
        <span class="user-badge">
          {{ user()?.username }}
          <span class="role-tag">{{ user()?.role }}</span>
        </span>
        <button class="btn-logout" (click)="logout.emit()">Logout</button>
      </div>
    </header>
  `,
  styles: [`
    .navbar {
      background: white;
      padding: 0 2rem;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-shadow: 0 1px 6px rgba(0,0,0,0.07);
      position: sticky;
      top: 0;
      z-index: 100;
    }
    .navbar-brand {
      font-size: 1.2rem;
      font-weight: 700;
      color: #1a1a2e;
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
    .brand-icon { font-size: 1.4rem; }
    .navbar-right {
      display: flex;
      align-items: center;
      gap: 1rem;
    }
    .user-badge {
      font-size: 0.85rem;
      color: #374151;
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }
    .role-tag {
      background: #ede9fe;
      color: #7c3aed;
      padding: 0.15rem 0.5rem;
      border-radius: 999px;
      font-size: 0.7rem;
      font-weight: 600;
      text-transform: uppercase;
    }
    .btn-logout {
      padding: 0.4rem 0.875rem;
      background: transparent;
      border: 1.5px solid #d1d5db;
      border-radius: 8px;
      font-size: 0.85rem;
      cursor: pointer;
      color: #6b7280;
      transition: all 0.2s;
    }
    .btn-logout:hover {
      border-color: #ef4444;
      color: #ef4444;
    }
    .btn-admin {
      padding: 0.6rem 1.5rem;
      background: #4f46e5;
      border: none;
      border-radius: 8px;
      font-size: 0.9rem;
      font-weight: 600;
      cursor: pointer;
      color: #ffffff;
      transition: background 0.2s;
      white-space: nowrap;
    }
    .btn-admin:hover {
      background: #4338ca;
    }
  `],
})
export class BoardNavbarComponent {
  readonly user = input<User | null>(null);
  readonly showAdminButton = input(false);
  readonly openAdmin = output<void>();
  readonly logout = output<void>();
}
