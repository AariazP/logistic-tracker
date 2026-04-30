import { Injectable, signal, computed } from '@angular/core';
import { Package, PackageStatus, PACKAGE_STATUSES } from '../../../shared/models';

@Injectable({ providedIn: 'root' })
export class PackageStore {
  private readonly _packages = signal<Package[]>([]);
  private readonly _loading = signal<boolean>(false);
  private readonly _error = signal<string | null>(null);

  // Public read-only computed signals
  readonly packages = computed(() => this._packages());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

  readonly packagesByStatus = computed(() => {
    const all = this._packages();
    return PACKAGE_STATUSES.reduce<Record<PackageStatus, Package[]>>(
      (acc, status) => {
        acc[status] = all.filter((p) => p.status === status);
        return acc;
      },
      {} as Record<PackageStatus, Package[]>,
    );
  });

  setPackages(packages: Package[]): void {
    this._packages.set(packages);
  }

  addPackage(pkg: Package): void {
    this._packages.update((current) => [...current, pkg]);
  }

  updatePackageStatus(id: string, status: PackageStatus): void {
    this._packages.update((current) =>
      current.map((p) => (p.id === id ? { ...p, status, updatedAt: new Date().toISOString() } : p)),
    );
  }

  revertPackageStatus(id: string, previousStatus: PackageStatus): void {
    this.updatePackageStatus(id, previousStatus);
  }

  setLoading(loading: boolean): void {
    this._loading.set(loading);
  }

  setError(error: string | null): void {
    this._error.set(error);
  }

  clearError(): void {
    this._error.set(null);
  }
}
