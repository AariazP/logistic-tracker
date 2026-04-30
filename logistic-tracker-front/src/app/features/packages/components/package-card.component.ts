import { Component, input } from '@angular/core';
import { Package } from '../../../shared/models';

@Component({
  selector: 'app-package-card',
  standalone: true,
  template: `
    <div class="package-card">
      <div class="card-header">
        <span class="tracking-id">{{ pkg().trackingId }}</span>
        <span class="status-badge" [class]="'status-' + pkg().status.toLowerCase()">
          {{ pkg().status }}
        </span>
      </div>
      <div class="card-body">
        <div class="info-row">
          <span class="label">Recipient</span>
          <span class="value">{{ pkg().recipientName }}</span>
        </div>
        <div class="info-row">
          <span class="label">Weight</span>
          <span class="value">{{ pkg().weight }} kg</span>
        </div>
        <div class="info-row">
          <span class="label">Dimensions</span>
          <span class="value">{{ pkg().dimensions }}</span>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .package-card {
      background: white;
      border-radius: 10px;
      padding: 0.875rem 1rem;
      box-shadow: 0 1px 6px rgba(0,0,0,0.07);
      border: 1.5px solid #e5e7eb;
      cursor: grab;
      transition: box-shadow 0.2s, transform 0.15s;
    }
    .package-card:hover {
      box-shadow: 0 4px 16px rgba(0,0,0,0.12);
      transform: translateY(-1px);
    }
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.75rem;
    }
    .tracking-id {
      font-weight: 700;
      font-size: 0.9rem;
      color: #1a1a2e;
    }
    .status-badge {
      font-size: 0.7rem;
      font-weight: 600;
      padding: 0.2rem 0.5rem;
      border-radius: 999px;
      text-transform: uppercase;
      letter-spacing: 0.04em;
    }
    .status-received { background: #eff6ff; color: #3b82f6; }
    .status-in_transit { background: #fefce8; color: #ca8a04; }
    .status-delivered { background: #f0fdf4; color: #16a34a; }
    .card-body {
      display: flex;
      flex-direction: column;
      gap: 0.35rem;
    }
    .info-row {
      display: flex;
      justify-content: space-between;
      font-size: 0.8rem;
    }
    .label { color: #9ca3af; }
    .value { color: #374151; font-weight: 500; }
  `],
})
export class PackageCardComponent {
  readonly pkg = input.required<Package>();
}
