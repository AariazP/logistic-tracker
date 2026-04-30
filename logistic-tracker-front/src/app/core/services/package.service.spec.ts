import { describe, it, expect, beforeEach, vi } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { PackageService } from './package.service';
import { PackageStore } from '../../features/packages/store/package.store';
import { Package } from '../../shared/models';

const mockPackage = (overrides: Partial<Package> = {}): Package => ({
  id: '1',
  trackingId: 'TRK-001',
  weight: 2.5,
  dimensions: '30x20x10',
  recipientName: 'Alice',
  status: 'RECEIVED',
  createdAt: '2024-01-01T00:00:00Z',
  updatedAt: '2024-01-01T00:00:00Z',
  ...overrides,
});

describe('PackageService', () => {
  let service: PackageService;
  let store: PackageStore;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(PackageService);
    store = TestBed.inject(PackageStore);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load packages and update store', () => {
    const packages = [mockPackage()];
    service.loadPackages().subscribe();

    const req = httpMock.expectOne((r) => r.url.includes('/packages'));
    req.flush(packages);

    expect(store.packages()).toEqual(packages);
    expect(store.loading()).toBe(false);
  });

  it('should set error on loadPackages failure', () => {
    service.loadPackages().subscribe({ error: () => {} });
    const req = httpMock.expectOne((r) => r.url.includes('/packages'));
    req.flush({ message: 'Server error' }, { status: 500, statusText: 'Internal Server Error' });

    expect(store.error()).toBeTruthy();
  });

  it('should add package to store on create', () => {
    const newPkg = mockPackage({ id: '99' });
    service.createPackage({ trackingId: 'TRK-099', weight: 1, dimensions: '10x10x10', recipientName: 'Bob' }).subscribe();

    const req = httpMock.expectOne((r) => r.url.includes('/packages'));
    req.flush(newPkg);

    expect(store.packages()).toContain(newPkg);
  });

  it('should reject invalid transition RECEIVED → DELIVERED', () => {
    store.setPackages([mockPackage({ id: '1', status: 'RECEIVED' })]);
    let errorCaught = false;
    service.movePackage('1', 'RECEIVED', 'DELIVERED').subscribe({
      error: (err: Error) => {
        expect(err.message).toContain('Invalid transition');
        errorCaught = true;
      },
    });
    expect(errorCaught).toBe(true);
  });

  it('should optimistically update and revert on backend error', () => {
    store.setPackages([mockPackage({ id: '1', status: 'RECEIVED' })]);
    service.movePackage('1', 'RECEIVED', 'IN_TRANSIT').subscribe({ error: () => {} });

    // Optimistic update applied
    expect(store.packages()[0].status).toBe('IN_TRANSIT');

    const req = httpMock.expectOne((r) => r.url.includes('/packages/1/status'));
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });

    // Reverted
    expect(store.packages()[0].status).toBe('RECEIVED');
    expect(store.error()).toBeTruthy();
  });
});
