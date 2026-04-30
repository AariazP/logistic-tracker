import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Package, CreatePackageRequest, UpdatePackageStatusRequest } from '../../shared/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PackageApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/packages`;

  getAll(): Observable<Package[]> {
    return this.http.get<Package[]>(this.baseUrl);
  }

  create(payload: CreatePackageRequest): Observable<Package> {
    return this.http.post<Package>(this.baseUrl, payload);
  }

  updateStatus(id: string, payload: UpdatePackageStatusRequest): Observable<Package> {
    return this.http.patch<Package>(`${this.baseUrl}/${id}/status`, payload);
  }
}
