import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateDriverRequest, Driver, Recipient, UpsertRecipientRequest } from '../../shared/models';

@Injectable({ providedIn: 'root' })
export class AdminApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/admin`;

  getRecipients(): Observable<Recipient[]> {
    return this.http.get<Recipient[]>(`${this.baseUrl}/recipients`);
  }

  createRecipient(payload: UpsertRecipientRequest): Observable<Recipient> {
    return this.http.post<Recipient>(`${this.baseUrl}/recipients`, payload);
  }

  updateRecipient(recipientId: string, payload: UpsertRecipientRequest): Observable<Recipient> {
    return this.http.put<Recipient>(`${this.baseUrl}/recipients/${recipientId}`, payload);
  }

  deleteRecipient(recipientId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/recipients/${recipientId}`);
  }

  getDrivers(): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${this.baseUrl}/drivers`);
  }

  createDriver(payload: CreateDriverRequest): Observable<Driver> {
    return this.http.post<Driver>(`${this.baseUrl}/drivers`, payload);
  }

  deleteDriver(driverId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/drivers/${driverId}`);
  }
}
