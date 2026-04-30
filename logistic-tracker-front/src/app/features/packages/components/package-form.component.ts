import { Component, OnInit, inject, signal, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PackageService } from '../../../core/services/package.service';
import { AdminApiService } from '../../../core/services/admin-api.service';
import { CreatePackageRequest, Recipient, getApiErrorMessage } from '../../../shared/models';

@Component({
  selector: 'app-package-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './package-form.component.html',
  styleUrl: './package-form.component.css',
})
export class PackageFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly packageService = inject(PackageService);
  private readonly adminApi = inject(AdminApiService);

  readonly packageCreated = output<void>();

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly success = signal(false);
  readonly recipients = signal<Recipient[]>([]);

  readonly form = this.fb.group({
    trackingId: ['', [Validators.required, Validators.maxLength(100)]],
    weight: [null as number | null, [Validators.required, Validators.min(0.01)]],
    dimensions: ['', [Validators.required, Validators.maxLength(255)]],
    recipientId: ['', [Validators.required]],
  });

  ngOnInit(): void {
    this.loadRecipients();
  }

  loadRecipients(): void {
    this.adminApi.getRecipients().subscribe({
      next: (items) => this.recipients.set(items),
      error: (err: unknown) => this.error.set(getApiErrorMessage(err, 'Failed to load recipients')),
    });
  }

  isInvalid(field: string): boolean {
    const c = this.form.get(field);
    return !!(c?.invalid && c?.touched);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.success.set(false);

    const payload = this.form.value as CreatePackageRequest;
    this.packageService.createPackage(payload).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set(true);
        this.form.reset();
        this.packageCreated.emit();
        setTimeout(() => this.success.set(false), 3000);
      },
      error: (err: unknown) => {
        this.loading.set(false);
        const msg = getApiErrorMessage(err, 'Failed to register package');
        this.error.set(msg);
      },
    });
  }
}
