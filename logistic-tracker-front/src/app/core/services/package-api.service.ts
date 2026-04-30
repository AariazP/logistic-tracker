import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Package, CreatePackageRequest, UpdatePackageStatusRequest, PackageStatus } from '../../shared/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PackageApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/packages`;

  getAll(status?: PackageStatus): Observable<Package[]> {
    const params = status ? new HttpParams().set('status', status) : undefined;
    return this.http.get<Package[]>(this.baseUrl, { params });
  }

  getByTrackingId(trackingId: string): Observable<Package> {
    return this.http.get<Package>(`${this.baseUrl}/${trackingId}`);
  }

  create(payload: CreatePackageRequest): Observable<Package> {
    return this.http.post<Package>(this.baseUrl, payload);
  }

  updateStatus(trackingId: string, payload: UpdatePackageStatusRequest): Observable<Package> {
    return this.http.patch<Package>(`${this.baseUrl}/${trackingId}/status`, payload);
  }
}
