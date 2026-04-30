import { describe, it, expect, beforeEach } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { PackageStore } from './package.store';
import { Package } from '../../../shared/models';

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

describe('PackageStore', () => {
  let store: PackageStore;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    store = TestBed.inject(PackageStore);
  });

  it('should initialize with empty packages', () => {
    expect(store.packages()).toEqual([]);
  });

  it('should set packages', () => {
    const packages = [mockPackage(), mockPackage({ id: '2', trackingId: 'TRK-002' })];
    store.setPackages(packages);
    expect(store.packages()).toHaveLength(2);
  });

  it('should add a package', () => {
    const pkg = mockPackage();
    store.addPackage(pkg);
    expect(store.packages()).toContain(pkg);
  });

  it('should update package status', () => {
    store.setPackages([mockPackage()]);
    store.updatePackageStatus('1', 'IN_TRANSIT');
    expect(store.packages()[0].status).toBe('IN_TRANSIT');
  });

  it('should revert package status', () => {
    store.setPackages([mockPackage({ status: 'IN_TRANSIT' })]);
    store.revertPackageStatus('1', 'RECEIVED');
    expect(store.packages()[0].status).toBe('RECEIVED');
  });

  it('should compute packagesByStatus correctly', () => {
    store.setPackages([
      mockPackage({ id: '1', status: 'RECEIVED' }),
      mockPackage({ id: '2', status: 'IN_TRANSIT' }),
      mockPackage({ id: '3', status: 'DELIVERED' }),
    ]);
    const byStatus = store.packagesByStatus();
    expect(byStatus.RECEIVED).toHaveLength(1);
    expect(byStatus.IN_TRANSIT).toHaveLength(1);
    expect(byStatus.DELIVERED).toHaveLength(1);
  });

  it('should set and clear error', () => {
    store.setError('Something went wrong');
    expect(store.error()).toBe('Something went wrong');
    store.clearError();
    expect(store.error()).toBeNull();
  });

  it('should set loading state', () => {
    store.setLoading(true);
    expect(store.loading()).toBe(true);
    store.setLoading(false);
    expect(store.loading()).toBe(false);
  });
});
