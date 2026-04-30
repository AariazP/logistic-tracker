import { Component, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PackageStatus } from '../../../../shared/models';

@Component({
  selector: 'app-package-search-filter',
  imports: [FormsModule],
  templateUrl: './package-search-filter.component.html',
  styleUrl: './package-search-filter.component.css',
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
