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

@Component({
  selector: 'app-board-page',
  standalone: true,
  imports: [
    BoardNavbarComponent,
    PackageFormComponent,
    PackageSearchFilterComponent,
    PackageBoardColumnsComponent,
  ],
  templateUrl: './board-page.component.html',
  styleUrl: './board-page.component.css',
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

  goToAdminManagement(): void {
    this.router.navigate(['/admin']);
  }

  logout(): void {
    this.authStore.clearAuth();
    this.router.navigate(['/login']);
  }
}
