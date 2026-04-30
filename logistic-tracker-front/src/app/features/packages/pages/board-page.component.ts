import { Component, OnInit, inject, signal } from '@angular/core';
import { CdkDrag, CdkDragDrop, CdkDropList } from '@angular/cdk/drag-drop';
import { PackageStore } from '../store/package.store';
import { PackageService } from '../../../core/services/package.service';
import { AuthStore } from '../../../core/services/auth.store';
import { PackageFormComponent } from '../components/package-form.component';
import { isValidTransition, Package, PackageStatus, PACKAGE_STATUSES } from '../../../shared/models';
import { Router } from '@angular/router';
import { BoardNavbarComponent } from '../components/organisms/board-navbar.component';
import { PackageSearchFilterComponent } from '../components/molecules/package-search-filter.component';
import { PackageBoardColumnsComponent } from '../components/organisms/package-board-columns.component';
import { AdminManagementComponent } from '../components/organisms/admin-management.component';

@Component({
  selector: 'app-board-page',
  standalone: true,
  imports: [
    BoardNavbarComponent,
    PackageFormComponent,
    PackageSearchFilterComponent,
    PackageBoardColumnsComponent,
    AdminManagementComponent,
  ],
  template: `
    <div class="board-layout">
      <app-board-navbar [user]="authStore.user()" (logout)="logout()" />

      <main class="board-content">
        @if (authStore.isAdmin()) {
          <app-package-form (packageCreated)="onPackageCreated()" />
          <app-admin-management />
        }

        <app-package-search-filter
          [trackingQuery]="trackingQuery()"
          [selectedStatus]="selectedStatus()"
          [statuses]="statuses"
          (trackingQueryChange)="trackingQuery.set($event)"
          (selectedStatusChange)="onStatusFilterChange($event)"
          (searchTracking)="searchByTrackingId()"
          (clearTracking)="clearTrackingSearch()"
          (refresh)="loadPackages()"
        />

        @if (selectedPackage()) {
          <section class="detail-card" aria-live="polite">
            <div class="detail-header">
              <strong>Tracking detail: {{ selectedPackage()!.trackingId }}</strong>
              <span class="status-chip">{{ selectedPackage()!.status }}</span>
            </div>
            <p>
              Recipient: {{ selectedPackage()!.recipientName }} | Weight: {{ selectedPackage()!.weight }} kg | Dimensions:
              {{ selectedPackage()!.dimensions }}
            </p>
          </section>
        }

        @if (store.error()) {
          <div class="global-error">
            <span>⚠ {{ store.error() }}</span>
            <button (click)="clearError()" class="close-btn">✕</button>
          </div>
        }

        @if (store.loading()) {
          <div class="loading-state">Loading packages...</div>
        }

        @if (!authStore.isDriver()) {
          <div class="info-banner">
            Drag and drop is available only for users with the DRIVER role.
          </div>
        }

        <app-package-board-columns
          [statuses]="statuses"
          [packagesByStatus]="store.packagesByStatus()"
          [connectedDropLists]="connectedDropLists"
          [draggable]="authStore.isDriver()"
          [canEnterDropList]="canEnterDropList"
          (dropToStatus)="onDrop($event.event, $event.status)"
        />
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
    .detail-card {
      background: #ffffff;
      border: 1px solid #dbeafe;
      border-radius: 10px;
      padding: 0.8rem 1rem;
      margin-bottom: 1rem;
      color: #1f2937;
    }
    .detail-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.35rem;
    }
    .status-chip {
      background: #eff6ff;
      color: #1d4ed8;
      font-size: 0.75rem;
      border-radius: 999px;
      padding: 0.15rem 0.5rem;
      font-weight: 600;
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
    @media (max-width: 768px) {
      .board-content { padding: 1rem; }
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
  readonly selectedStatus = signal<PackageStatus | null>(null);
  readonly trackingQuery = signal('');
  readonly selectedPackage = signal<Package | null>(null);

  ngOnInit(): void {
    this.loadPackages();
  }

  loadPackages(): void {
    this.packageService.loadPackages(this.selectedStatus() ?? undefined).subscribe();
  }

  readonly canEnterDropList = (drag: CdkDrag<Package>, drop: CdkDropList<Package[]>): boolean => {
    if (!this.authStore.isDriver()) return false;

    const pkg = drag.data;
    const targetStatus = drop.id as PackageStatus;

    if (!pkg) return false;
    if (pkg.status === targetStatus) return true;

    return isValidTransition(pkg.status, targetStatus);
  };

  onStatusFilterChange(status: PackageStatus | null): void {
    this.selectedStatus.set(status);
    this.loadPackages();
  }

  searchByTrackingId(): void {
    const trackingId = this.trackingQuery().trim();
    if (!trackingId) {
      this.selectedPackage.set(null);
      this.loadPackages();
      return;
    }

    this.packageService.getPackageByTrackingId(trackingId).subscribe({
      next: (pkg) => this.selectedPackage.set(pkg),
    });
  }

  clearTrackingSearch(): void {
    this.trackingQuery.set('');
    this.selectedPackage.set(null);
    this.loadPackages();
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
    if (!isValidTransition(fromStatus, targetStatus)) {
      this.store.setError(`Invalid transition: ${fromStatus} → ${targetStatus}`);
      return;
    }

    this.packageService.movePackage(pkg, targetStatus).subscribe({
      error: () => {
        // Store revert handles signal state; CDK list is refreshed via signal
      },
    });
  }

  onPackageCreated(): void {
    this.loadPackages();
  }

  clearError(): void {
    this.store.clearError();
  }

  logout(): void {
    this.authStore.clearAuth();
    this.router.navigate(['/login']);
  }
}
