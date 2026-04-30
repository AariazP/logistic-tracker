import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AdminManagementComponent } from '../components/organisms/admin-management.component';

@Component({
  selector: 'app-admin-page',
  imports: [AdminManagementComponent],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css',
})
export class AdminPageComponent {
  private readonly router = inject(Router);

  goBackToBoard(): void {
    this.router.navigate(['/board']);
  }
}
