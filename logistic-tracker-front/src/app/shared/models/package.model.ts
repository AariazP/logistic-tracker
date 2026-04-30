export type PackageStatus = 'RECEIVED' | 'IN_TRANSIT' | 'DELIVERED';

export interface Package {
  id: string;
  trackingId: string;
  weight: number;
  dimensions: string;
  recipientName: string;
  status: PackageStatus;
  createdAt: string;
  updatedAt: string;
}

export interface CreatePackageRequest {
  trackingId: string;
  weight: number;
  dimensions: string;
  recipientName: string;
}

export interface UpdatePackageStatusRequest {
  status: PackageStatus;
}

export const PACKAGE_STATUSES: PackageStatus[] = ['RECEIVED', 'IN_TRANSIT', 'DELIVERED'];

export const VALID_TRANSITIONS: Record<PackageStatus, PackageStatus[]> = {
  RECEIVED: ['IN_TRANSIT'],
  IN_TRANSIT: ['DELIVERED'],
  DELIVERED: [],
};

export function isValidTransition(from: PackageStatus, to: PackageStatus): boolean {
  return VALID_TRANSITIONS[from].includes(to);
}
