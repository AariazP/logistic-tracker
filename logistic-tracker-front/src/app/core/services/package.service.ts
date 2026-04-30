import { Injectable, inject } from '@angular/core';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { PackageApiService } from './package-api.service';
import { PackageStore } from '../../features/packages/store/package.store';
import { Package, PackageStatus, CreatePackageRequest, isValidTransition, getApiErrorMessage } from '../../shared/models';

@Injectable({ providedIn: 'root' })
export class PackageService {
  private readonly api = inject(PackageApiService);
  private readonly store = inject(PackageStore);

  loadPackages(status?: PackageStatus): Observable<Package[]> {
    this.store.setLoading(true);
    this.store.clearError();
    return this.api.getAll(status).pipe(
      tap((packages) => {
        this.store.setPackages(packages);
        this.store.setLoading(false);
      }),
      catchError((err) => {
        this.store.setLoading(false);
        this.store.setError(getApiErrorMessage(err, 'Failed to load packages'));
        return throwError(() => err);
      }),
    );
  }

  getPackageByTrackingId(trackingId: string): Observable<Package> {
    this.store.clearError();
    return this.api.getByTrackingId(trackingId).pipe(
      catchError((err) => {
        this.store.setError(getApiErrorMessage(err, 'Failed to fetch package detail'));
        return throwError(() => err);
      }),
    );
  }

  createPackage(payload: CreatePackageRequest): Observable<Package> {
    this.store.clearError();
    return this.api.create(payload).pipe(
      tap((pkg) => this.store.addPackage(pkg)),
      catchError((err) => {
        this.store.setError(getApiErrorMessage(err, 'Failed to create package'));
        return throwError(() => err);
      }),
    );
  }

  movePackage(pkg: Package, toStatus: PackageStatus): Observable<Package> {
    const fromStatus = pkg.status;
    const packageId = pkg.id;

    if (!isValidTransition(fromStatus, toStatus)) {
      const msg = `Invalid transition: ${fromStatus} → ${toStatus}`;
      this.store.setError(msg);
      return throwError(() => new Error(msg));
    }

    // Optimistic update
    this.store.updatePackageStatus(packageId, toStatus);
    this.store.clearError();

    return this.api.updateStatus(pkg.trackingId, { status: toStatus }).pipe(
      tap((updated) => this.store.updatePackageStatus(packageId, updated.status)),
      catchError((err) => {
        // Revert on failure
        this.store.revertPackageStatus(packageId, fromStatus);
        this.store.setError(getApiErrorMessage(err, 'Failed to move package'));
        return throwError(() => err);
      }),
    );
  }
}
