import { Injectable, inject } from '@angular/core';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { PackageApiService } from './package-api.service';
import { PackageStore } from '../../features/packages/store/package.store';
import { Package, PackageStatus, CreatePackageRequest, isValidTransition } from '../../shared/models';

@Injectable({ providedIn: 'root' })
export class PackageService {
  private readonly api = inject(PackageApiService);
  private readonly store = inject(PackageStore);

  loadPackages(): Observable<Package[]> {
    this.store.setLoading(true);
    this.store.clearError();
    return this.api.getAll().pipe(
      tap((packages) => {
        this.store.setPackages(packages);
        this.store.setLoading(false);
      }),
      catchError((err) => {
        this.store.setLoading(false);
        this.store.setError(err.error?.message ?? 'Failed to load packages');
        return throwError(() => err);
      }),
    );
  }

  createPackage(payload: CreatePackageRequest): Observable<Package> {
    this.store.clearError();
    return this.api.create(payload).pipe(
      tap((pkg) => this.store.addPackage(pkg)),
      catchError((err) => {
        this.store.setError(err.error?.message ?? 'Failed to create package');
        return throwError(() => err);
      }),
    );
  }

  movePackage(id: string, fromStatus: PackageStatus, toStatus: PackageStatus): Observable<Package> {
    if (!isValidTransition(fromStatus, toStatus)) {
      const msg = `Invalid transition: ${fromStatus} → ${toStatus}`;
      this.store.setError(msg);
      return throwError(() => new Error(msg));
    }

    // Optimistic update
    this.store.updatePackageStatus(id, toStatus);
    this.store.clearError();

    return this.api.updateStatus(id, { status: toStatus }).pipe(
      tap((updated) => this.store.updatePackageStatus(id, updated.status)),
      catchError((err) => {
        // Revert on failure
        this.store.revertPackageStatus(id, fromStatus);
        this.store.setError(err.error?.message ?? 'Failed to move package');
        return throwError(() => err);
      }),
    );
  }
}
