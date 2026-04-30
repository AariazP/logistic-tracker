import { describe, it, expect, beforeEach } from 'vitest';
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
  recipientId: 'recipient-1',
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

  it('should load packages with status filter and clear loading', () => {
    const packages = [mockPackage({ status: 'IN_TRANSIT' })];

    service.loadPackages('IN_TRANSIT').subscribe();

    const req = httpMock.expectOne((r) => r.url.includes('/packages'));
    expect(req.request.params.get('status')).toBe('IN_TRANSIT');
    req.flush(packages);

    expect(store.packages()).toEqual(packages);
    expect(store.loading()).toBe(false);
    expect(store.error()).toBeNull();
  });

  it('should add package to store on create', () => {
    const newPkg = mockPackage({ id: '99' });
    service.createPackage({ trackingId: 'TRK-099', weight: 1, dimensions: '10x10x10', recipientId: 'recipient-99' }).subscribe();

    const req = httpMock.expectOne((r) => r.url.includes('/packages'));
    req.flush(newPkg);

    expect(store.packages()).toContain(newPkg);
  });

  it('should set error on create failure', () => {
    service.createPackage({ trackingId: 'TRK-099', weight: 1, dimensions: '10x10x10', recipientId: 'recipient-99' }).subscribe({
      error: () => {},
    });

    const req = httpMock.expectOne((r) => r.url.includes('/packages'));
    req.flush({ message: 'validation error' }, { status: 400, statusText: 'Bad Request' });

    expect(store.error()).toBeTruthy();
  });

  it('should fetch package by tracking id', () => {
    const pkg = mockPackage({ trackingId: 'TRK-200' });

    service.getPackageByTrackingId('TRK-200').subscribe((res) => {
      expect(res).toEqual(pkg);
    });

    const req = httpMock.expectOne((r) => r.url.includes('/packages/TRK-200'));
    req.flush(pkg);
  });

  it('should set error on getPackageByTrackingId failure', () => {
    service.getPackageByTrackingId('TRK-404').subscribe({ error: () => {} });

    const req = httpMock.expectOne((r) => r.url.includes('/packages/TRK-404'));
    req.flush({ message: 'not found' }, { status: 404, statusText: 'Not Found' });

    expect(store.error()).toBeTruthy();
  });

  it('should reject invalid transition RECEIVED → DELIVERED', () => {
    const pkg = mockPackage({ id: '1', trackingId: 'TRK-001', status: 'RECEIVED' });
    store.setPackages([pkg]);
    let errorCaught = false;
    service.movePackage(pkg, 'DELIVERED').subscribe({
      error: (err: Error) => {
        expect(err.message).toContain('Invalid transition');
        errorCaught = true;
      },
    });
    expect(errorCaught).toBe(true);
  });

  it('should optimistically update and revert on backend error', () => {
    const pkg = mockPackage({ id: '1', trackingId: 'TRK-001', status: 'RECEIVED' });
    store.setPackages([pkg]);
    service.movePackage(pkg, 'IN_TRANSIT').subscribe({ error: () => {} });

    // Optimistic update applied
    expect(store.packages()[0].status).toBe('IN_TRANSIT');

    const req = httpMock.expectOne((r) => r.url.includes('/packages/TRK-001/status'));
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });

    // Reverted
    expect(store.packages()[0].status).toBe('RECEIVED');
    expect(store.error()).toBeTruthy();
  });

  it('should keep updated status on successful move', () => {
    const pkg = mockPackage({ id: '1', trackingId: 'TRK-001', status: 'RECEIVED' });
    store.setPackages([pkg]);

    service.movePackage(pkg, 'IN_TRANSIT').subscribe((updated) => {
      expect(updated.status).toBe('IN_TRANSIT');
    });

    const req = httpMock.expectOne((r) => r.url.includes('/packages/TRK-001/status'));
    req.flush({ ...pkg, status: 'IN_TRANSIT' });

    expect(store.packages()[0].status).toBe('IN_TRANSIT');
    expect(store.error()).toBeNull();
  });
});
