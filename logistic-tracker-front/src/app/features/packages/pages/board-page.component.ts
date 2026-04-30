import { Component, inject, OnInit, signal } from '@angular/core';
import { CdkDragDrop, DragDropModule, transferArrayItem } from '@angular/cdk/drag-drop';
import { PackageStore } from '../store/package.store';
import { PackageService } from '../../../core/services/package.service';
import { AuthStore } from '../../../core/services/auth.store';
import { PackageFormComponent } from '../components/package-form.component';
import { PackageCardComponent } from '../components/package-card.component';
import { Package, PackageStatus, PACKAGE_STATUSES } from '../../../shared/models';
import { Router } from '@angular/router';

@Component({
  selector: 'app-board-page',
  standalone: true,
  imports: [DragDropModule, PackageFormComponent, PackageCardComponent],
  template: `
    <div class="board-layout">
      <!-- Navbar -->
      <header class="navbar">
        <div class="navbar-brand">
          <span class="brand-icon">📦</span>
          <span>SmartShip</span>
        </div>
        <div class="navbar-right">
          <span class="user-badge">
            {{ authStore.user()?.username }}
            <span class="role-tag">{{ authStore.user()?.role }}</span>
          </span>
          <button class="btn-logout" (click)="logout()">Logout</button>
        </div>
      </header>

      <main class="board-content">
        <!-- Package Registration Form - ADMIN only -->
        @if (authStore.isAdmin()) {
          <app-package-form (packageCreated)="onPackageCreated()" />
        }

        <!-- Error Banner -->
        @if (store.error()) {
          <div class="global-error">
            <span>⚠ {{ store.error() }}</span>
            <button (click)="clearError()" class="close-btn">✕</button>
          </div>
        }

        <!-- Loading State -->
        @if (store.loading()) {
          <div class="loading-state">Loading packages...</div>
        }

        @if (!authStore.isDriver()) {
          <div class="info-banner">
            Drag and drop is available only for users with the DRIVER role.
          </div>
        }

        <!-- Logistics Board -->
        <div class="board-columns">
          @for (status of statuses; track status) {
            <div class="column">
              <div class="column-header">
                <span class="column-title">{{ status.replace('_', ' ') }}</span>
                <span class="column-count">{{ store.packagesByStatus()[status]?.length ?? 0 }}</span>
              </div>

              <div
                class="column-body"
                cdkDropList
                [id]="status"
                [cdkDropListData]="getColumnPackages(status)"
                [cdkDropListConnectedTo]="connectedDropLists"
                (cdkDropListDropped)="onDrop($event, status)"
                [class.cdk-drop-list-dragging]="isDragging()"
              >
                @for (pkg of store.packagesByStatus()[status]; track pkg.id) {
                  <div
                    cdkDrag
                    [cdkDragData]="pkg"
                    [cdkDragDisabled]="!authStore.isDriver()"
                    (cdkDragStarted)="isDragging.set(true)"
                    (cdkDragEnded)="isDragging.set(false)"
                    class="drag-item"
                  >
                    <app-package-card [pkg]="pkg" [draggable]="authStore.isDriver()" />
                    <div *cdkDragPlaceholder class="drag-placeholder"></div>
                  </div>
                }

                @if (!store.packagesByStatus()[status]?.length) {
                  <div class="empty-column">Drop packages here</div>
                }
              </div>
            </div>
          }
        </div>
      </main>
    </div>
  `,
  styles: [`
    .board-layout {
      min-height: 100vh;
      background: #f0f2f5;
      display: flex;
      flex-direction: column;
    }
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
    .board-content {
      padding: 1.5rem 2rem;
      flex: 1;
    }
    .global-error {
      background: #fef2f2;
      border: 1px solid #fecaca;
      color: #dc2626;
      padding: 0.75rem 1rem;
      border-radius: 8px;
      margin-bottom: 1rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 0.875rem;
    }
    .close-btn {
      background: none;
      border: none;
      cursor: pointer;
      color: #dc2626;
      font-size: 1rem;
      padding: 0;
    }
    .loading-state {
      text-align: center;
      color: #6b7280;
      padding: 2rem;
    }
    .info-banner {
      background: #eff6ff;
      border: 1px solid #bfdbfe;
      color: #1d4ed8;
      padding: 0.65rem 0.85rem;
      border-radius: 8px;
      margin-bottom: 1rem;
      font-size: 0.85rem;
    }
    .board-columns {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 1.25rem;
      align-items: start;
    }
    @media (max-width: 768px) {
      .board-columns { grid-template-columns: 1fr; }
      .board-content { padding: 1rem; }
    }
    .column {
      background: #f8fafc;
      border-radius: 12px;
      border: 1px solid #e5e7eb;
      overflow: hidden;
    }
    .column-header {
      padding: 0.875rem 1rem;
      background: white;
      border-bottom: 1px solid #e5e7eb;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .column-title {
      font-weight: 700;
      font-size: 0.85rem;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      color: #374151;
    }
    .column-count {
      background: #e5e7eb;
      color: #6b7280;
      font-size: 0.75rem;
      font-weight: 600;
      padding: 0.15rem 0.5rem;
      border-radius: 999px;
    }
    .column-body {
      padding: 0.875rem;
      min-height: 200px;
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
      transition: background 0.2s;
    }
    .column-body.cdk-drop-list-receiving {
      background: #eff6ff;
      border: 2px dashed #3b82f6;
      border-radius: 8px;
    }
    .empty-column {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 80px;
      color: #d1d5db;
      font-size: 0.85rem;
      border: 2px dashed #e5e7eb;
      border-radius: 8px;
    }
    .drag-placeholder {
      height: 80px;
      background: #eff6ff;
      border: 2px dashed #93c5fd;
      border-radius: 10px;
    }
    .drag-item.cdk-drag-disabled {
      opacity: 0.9;
    }
    .cdk-drag-animating {
      transition: transform 250ms cubic-bezier(0, 0, 0.2, 1);
    }
    .cdk-drag-preview {
      box-shadow: 0 8px 32px rgba(0,0,0,0.18);
      border-radius: 10px;
      opacity: 0.95;
    }
  `],
})
export class BoardPageComponent implements OnInit {
  protected readonly store = inject(PackageStore);
  private readonly packageService = inject(PackageService);
  protected readonly authStore = inject(AuthStore);
  private readonly router = inject(Router);

  readonly statuses: PackageStatus[] = PACKAGE_STATUSES;
  readonly connectedDropLists = PACKAGE_STATUSES;
  readonly isDragging = signal(false);

  ngOnInit(): void {
    this.packageService.loadPackages().subscribe();
  }

  getColumnPackages(status: PackageStatus): Package[] {
    return this.store.packagesByStatus()[status] ?? [];
  }

  onDrop(event: CdkDragDrop<Package[]>, targetStatus: PackageStatus): void {
    if (event.previousContainer === event.container) return;
    if (!this.authStore.isDriver()) {
      this.store.setError('Only DRIVER users can update package status.');
      return;
    }

    const pkg: Package = event.item.data;
    const fromStatus = pkg.status;

    if (fromStatus === targetStatus) return;

    // Optimistic UI update via CDK
    transferArrayItem(
      event.previousContainer.data,
      event.container.data,
      event.previousIndex,
      event.currentIndex,
    );

    this.packageService.movePackage(pkg, targetStatus).subscribe({
      error: () => {
        // Store revert handles signal state; CDK list is refreshed via signal
      },
    });
  }

  onPackageCreated(): void {
    // Board auto-refreshes via signal state
  }

  clearError(): void {
    this.store.clearError();
  }

  logout(): void {
    this.authStore.clearAuth();
    this.router.navigate(['/login']);
  }
}
