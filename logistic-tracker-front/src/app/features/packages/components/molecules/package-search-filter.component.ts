import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PackageStatus } from '../../../../shared/models';

@Component({
  selector: 'app-package-search-filter',
  imports: [FormsModule],
  template: `
    <section class="toolbar" aria-label="Package tools">
      <div class="toolbar-group">
        <label class="field-label" for="tracking-search">Search by tracking ID</label>
        <div class="inline-row">
          <input
            id="tracking-search"
            class="text-input"
            type="text"
            [ngModel]="trackingQuery()"
            (ngModelChange)="trackingQueryChange.emit($event)"
            placeholder="e.g. TRK-2026-001"
          />
          <button type="button" class="btn-primary" (click)="searchTracking.emit()">Search</button>
          <button type="button" class="btn-ghost" (click)="clearTracking.emit()">Clear</button>
        </div>
      </div>

      <div class="toolbar-group">
        <label class="field-label" for="status-filter">Filter by status</label>
        <div class="inline-row">
          <select
            id="status-filter"
            class="text-input"
            [ngModel]="selectedStatus()"
            (ngModelChange)="selectedStatusChange.emit($event)"
          >
            <option [ngValue]="null">All statuses</option>
            @for (status of statuses(); track status) {
              <option [ngValue]="status">{{ status }}</option>
            }
          </select>
          <button type="button" class="btn-ghost" (click)="refresh.emit()">Refresh</button>
        </div>
      </div>
    </section>
  `,
  styles: [`
    .toolbar {
      background: white;
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 1rem;
      margin-bottom: 1rem;
      display: grid;
      gap: 0.9rem;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    }
    .toolbar-group {
      display: flex;
      flex-direction: column;
      gap: 0.4rem;
    }
    .field-label {
      color: #374151;
      font-size: 0.8rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.04em;
    }
    .inline-row {
      display: flex;
      gap: 0.5rem;
      flex-wrap: wrap;
    }
    .text-input {
      flex: 1;
      min-width: 160px;
      border: 1.5px solid #d1d5db;
      border-radius: 8px;
      padding: 0.52rem 0.72rem;
      font-size: 0.9rem;
      outline: none;
      background: white;
    }
    .text-input:focus {
      border-color: #4f46e5;
    }
    .btn-primary,
    .btn-ghost {
      border-radius: 8px;
      font-size: 0.85rem;
      font-weight: 600;
      padding: 0.5rem 0.9rem;
      cursor: pointer;
      border: 1px solid transparent;
    }
    .btn-primary {
      background: #4f46e5;
      color: white;
    }
    .btn-primary:hover {
      background: #4338ca;
    }
    .btn-ghost {
      background: white;
      border-color: #d1d5db;
      color: #6b7280;
    }
    .btn-ghost:hover {
      border-color: #6b7280;
      color: #374151;
    }
  `],
})
export class PackageSearchFilterComponent {
  readonly trackingQuery = input<string>('');
  readonly selectedStatus = input<PackageStatus | null>(null);
  readonly statuses = input<PackageStatus[]>([]);

  readonly trackingQueryChange = output<string>();
  readonly selectedStatusChange = output<PackageStatus | null>();
  readonly searchTracking = output<void>();
  readonly clearTracking = output<void>();
  readonly refresh = output<void>();
}
