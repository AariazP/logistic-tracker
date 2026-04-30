import { Component, input, output } from '@angular/core';
import { User } from '../../../../shared/models';

@Component({
  selector: 'app-board-navbar',
  templateUrl: './board-navbar.component.html',
  styleUrl: './board-navbar.component.css',
})
export class BoardNavbarComponent {
  readonly user = input<User | null>(null);
  readonly showAdminButton = input(false);
  readonly openAdmin = output<void>();
  readonly logout = output<void>();
}
