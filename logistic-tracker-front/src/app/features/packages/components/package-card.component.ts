import { Component, input } from '@angular/core';
import { Package } from '../../../shared/models';

@Component({
  selector: 'app-package-card',
  standalone: true,
  templateUrl: './package-card.component.html',
  styleUrl: './package-card.component.css',
})
export class PackageCardComponent {
  readonly pkg = input.required<Package>();
  readonly draggable = input<boolean>(true);
}
