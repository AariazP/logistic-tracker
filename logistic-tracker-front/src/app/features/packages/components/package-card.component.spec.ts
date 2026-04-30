import { describe, it, expect, beforeEach } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { ComponentFixture } from '@angular/core/testing';
import { PackageCardComponent } from './package-card.component';
import { Package } from '../../../shared/models';

const mockPackage: Package = {
  id: '1',
  trackingId: 'TRK-001',
  weight: 2.5,
  dimensions: '30x20x10',
  recipientName: 'Alice Smith',
  status: 'RECEIVED',
  createdAt: '2024-01-01T00:00:00Z',
  updatedAt: '2024-01-01T00:00:00Z',
};

describe('PackageCardComponent', () => {
  let fixture: ComponentFixture<PackageCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PackageCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PackageCardComponent);
    fixture.componentRef.setInput('pkg', mockPackage);
    fixture.detectChanges();
  });

  it('should display tracking id', () => {
    const el: HTMLElement = fixture.nativeElement;
    expect(el.textContent).toContain('TRK-001');
  });

  it('should display recipient name', () => {
    const el: HTMLElement = fixture.nativeElement;
    expect(el.textContent).toContain('Alice Smith');
  });

  it('should display weight', () => {
    const el: HTMLElement = fixture.nativeElement;
    expect(el.textContent).toContain('2.5 kg');
  });

  it('should display dimensions', () => {
    const el: HTMLElement = fixture.nativeElement;
    expect(el.textContent).toContain('30x20x10');
  });

  it('should apply correct status CSS class', () => {
    const badge = fixture.nativeElement.querySelector('.status-badge');
    expect(badge.classList).toContain('status-received');
  });

  it('should update display when status changes', () => {
    fixture.componentRef.setInput('pkg', { ...mockPackage, status: 'IN_TRANSIT' });
    fixture.detectChanges();
    const badge = fixture.nativeElement.querySelector('.status-badge');
    expect(badge.classList).toContain('status-in_transit');
  });
});
