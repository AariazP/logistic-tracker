import { Component, inject, signal, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PackageService } from '../../../core/services/package.service';
import { CreatePackageRequest, getApiErrorMessage } from '../../../shared/models';

@Component({
  selector: 'app-package-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="form-card">
      <h2 class="form-title">Register New Package</h2>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="package-form">
        <div class="fields-grid">
          <div class="field">
            <label for="trackingId">Tracking ID</label>
            <input
              id="trackingId"
              type="text"
              formControlName="trackingId"
              placeholder="e.g. TRK-001"
              [class.invalid]="isInvalid('trackingId')"
            />
            @if (isInvalid('trackingId')) {
              <span class="field-error">Tracking ID is required</span>
            }
          </div>

          <div class="field">
            <label for="weight">Weight (kg)</label>
            <input
              id="weight"
              type="number"
              formControlName="weight"
              placeholder="e.g. 2.5"
              [class.invalid]="isInvalid('weight')"
            />
            @if (isInvalid('weight')) {
              <span class="field-error">Weight must be greater than 0</span>
            }
          </div>

          <div class="field">
            <label for="dimensions">Dimensions</label>
            <input
              id="dimensions"
              type="text"
              formControlName="dimensions"
              placeholder="e.g. 30x20x15 cm"
              [class.invalid]="isInvalid('dimensions')"
            />
            @if (isInvalid('dimensions')) {
              <span class="field-error">Dimensions are required</span>
            }
          </div>

          <div class="field">
            <label for="recipientName">Recipient Name</label>
            <input
              id="recipientName"
              type="text"
              formControlName="recipientName"
              placeholder="Full name"
              [class.invalid]="isInvalid('recipientName')"
            />
            @if (isInvalid('recipientName')) {
              <span class="field-error">Recipient name is required</span>
            }
          </div>
        </div>

        @if (error()) {
          <div class="error-banner">{{ error() }}</div>
        }
        @if (success()) {
          <div class="success-banner">Package registered successfully!</div>
        }

        <div class="form-actions">
          <button type="submit" [disabled]="loading()" class="btn-primary">
            @if (loading()) { Saving... } @else { Register Package }
          </button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .form-card {
      background: white;
      border-radius: 12px;
      padding: 1.5rem 2rem;
      box-shadow: 0 2px 12px rgba(0,0,0,0.06);
      margin-bottom: 1.5rem;
    }
    .form-title {
      font-size: 1.1rem;
      font-weight: 600;
      color: #1a1a2e;
      margin: 0 0 1.25rem;
    }
    .package-form {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }
    .fields-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 1rem;
    }
    .field {
      display: flex;
      flex-direction: column;
      gap: 0.35rem;
    }
    .field label {
      font-size: 0.8rem;
      font-weight: 500;
      color: #374151;
      text-transform: uppercase;
      letter-spacing: 0.03em;
    }
    .field input {
      padding: 0.55rem 0.75rem;
      border: 1.5px solid #d1d5db;
      border-radius: 8px;
      font-size: 0.9rem;
      outline: none;
      transition: border-color 0.2s;
    }
    .field input:focus { border-color: #4f46e5; }
    .field input.invalid { border-color: #ef4444; }
    .field-error { font-size: 0.75rem; color: #ef4444; }
    .error-banner {
      background: #fef2f2;
      border: 1px solid #fecaca;
      color: #dc2626;
      padding: 0.65rem 0.875rem;
      border-radius: 8px;
      font-size: 0.85rem;
    }
    .success-banner {
      background: #f0fdf4;
      border: 1px solid #bbf7d0;
      color: #16a34a;
      padding: 0.65rem 0.875rem;
      border-radius: 8px;
      font-size: 0.85rem;
    }
    .form-actions { display: flex; justify-content: flex-end; }
    .btn-primary {
      padding: 0.6rem 1.5rem;
      background: #4f46e5;
      color: white;
      border: none;
      border-radius: 8px;
      font-size: 0.9rem;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
    }
    .btn-primary:hover:not(:disabled) { background: #4338ca; }
    .btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
  `],
})
export class PackageFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly packageService = inject(PackageService);

  readonly packageCreated = output<void>();

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly success = signal(false);

  readonly form = this.fb.group({
    trackingId: ['', [Validators.required, Validators.maxLength(100)]],
    weight: [null as number | null, [Validators.required, Validators.min(0.01)]],
    dimensions: ['', [Validators.required, Validators.maxLength(255)]],
    recipientName: ['', [Validators.required, Validators.maxLength(255)]],
  });

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
