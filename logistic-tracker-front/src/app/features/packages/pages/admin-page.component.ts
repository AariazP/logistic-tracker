import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AdminManagementComponent } from '../components/organisms/admin-management.component';

@Component({
  selector: 'app-admin-page',
  imports: [AdminManagementComponent],
  template: `
    <div class="admin-layout">
      <main class="admin-content">
        <button
          type="button"
          class="back-icon-btn"
          aria-label="Back to board"
          title="Back to board"
          (click)="goBackToBoard()"
        >
          ←
        </button>

        <app-admin-management />
      </main>
    </div>
  `,
  styles: [`
    .admin-layout {
      min-height: 100vh;
      background: #f0f2f5;
      display: flex;
      flex-direction: column;
    }
    .admin-content {
      padding: 1.5rem 2rem;
      flex: 1;
    }
    .back-icon-btn {
      width: 36px;
      height: 36px;
      border-radius: 999px;
      border: 1px solid #d1d5db;
      background: #ffffff;
      color: #111827;
      font-size: 1rem;
      font-weight: 700;
      cursor: pointer;
      margin-bottom: 0.9rem;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }
    .back-icon-btn:hover {
      border-color: #9ca3af;
    }
    @media (max-width: 768px) {
      .admin-content {
        padding: 1rem;
      }
    }
  `],
})
export class AdminPageComponent {
  private readonly router = inject(Router);

  goBackToBoard(): void {
    this.router.navigate(['/board']);
  }
}
