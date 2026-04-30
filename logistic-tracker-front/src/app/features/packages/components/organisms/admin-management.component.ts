import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminApiService } from '../../../../core/services/admin-api.service';
import {
  CreateDriverRequest,
  Driver,
  Recipient,
  UpsertRecipientRequest,
  getApiErrorMessage,
} from '../../../../shared/models';

@Component({
  selector: 'app-admin-management',
  imports: [ReactiveFormsModule, DatePipe],
  template: `
    <section class="admin-grid">
      <article class="panel">
        <h3 class="panel-title">Recipients</h3>

        <form [formGroup]="recipientForm" (ngSubmit)="saveRecipient()" class="stack">
          <div class="fields-grid">
            <label class="field">
              <span>Name</span>
              <input formControlName="name" type="text" />
            </label>
            <label class="field">
              <span>Email</span>
              <input formControlName="email" type="email" />
            </label>
            <label class="field">
              <span>Phone</span>
              <input formControlName="phone" type="text" />
            </label>
            <label class="field">
              <span>Address</span>
              <input formControlName="address" type="text" />
            </label>
            <label class="field">
              <span>Document</span>
              <input formControlName="documentNumber" type="text" />
            </label>
          </div>
          <div class="row-actions">
            <button type="submit" class="btn-primary" [disabled]="recipientSaving()">
              {{ editingRecipientId() ? 'Update recipient' : 'Create recipient' }}
            </button>
            @if (editingRecipientId()) {
              <button type="button" class="btn-ghost" (click)="cancelRecipientEdit()">Cancel edit</button>
            }
          </div>
        </form>

        <div class="list-wrap">
          @for (recipient of recipients(); track recipient.id) {
            <div class="list-item">
              <div>
                <strong>{{ recipient.name }}</strong>
                <p>{{ recipient.email }} | {{ recipient.phone }}</p>
              </div>
              <div class="row-actions">
                <button type="button" class="btn-ghost" (click)="editRecipient(recipient)">Edit</button>
                <button type="button" class="btn-danger" (click)="deleteRecipient(recipient.id)">Delete</button>
              </div>
            </div>
          }
        </div>
      </article>

      <article class="panel">
        <h3 class="panel-title">Drivers</h3>

        <form [formGroup]="driverForm" (ngSubmit)="createDriver()" class="stack">
          <div class="fields-grid two-col">
            <label class="field">
              <span>Username</span>
              <input formControlName="username" type="text" />
            </label>
            <label class="field">
              <span>Password</span>
              <input formControlName="password" type="password" />
            </label>
          </div>
          <button type="submit" class="btn-primary" [disabled]="driverSaving()">Create driver</button>
        </form>

        <div class="list-wrap">
          @for (driver of drivers(); track driver.id) {
            <div class="list-item">
              <div>
                <strong>{{ driver.username }}</strong>
                <p>{{ driver.role }} | {{ driver.createdAt | date: 'short' }}</p>
              </div>
              <button type="button" class="btn-danger" (click)="deleteDriver(driver.id)">Delete</button>
            </div>
          }
        </div>
      </article>
    </section>

    @if (error()) {
      <div class="global-error">{{ error() }}</div>
    }
  `,
  styles: [`
    .admin-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
      gap: 1rem;
      margin-bottom: 1rem;
    }
    .panel {
      background: white;
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 1rem;
      display: flex;
      flex-direction: column;
      gap: 0.9rem;
    }
    .panel-title {
      margin: 0;
      font-size: 1rem;
      color: #1f2937;
    }
    .stack {
      display: flex;
      flex-direction: column;
      gap: 0.7rem;
    }
    .fields-grid {
      display: grid;
      gap: 0.65rem;
      grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
    }
    .fields-grid.two-col {
      grid-template-columns: repeat(2, minmax(140px, 1fr));
    }
    .field {
      display: flex;
      flex-direction: column;
      gap: 0.3rem;
      font-size: 0.8rem;
      color: #374151;
      text-transform: uppercase;
      letter-spacing: 0.03em;
    }
    .field input {
      border: 1.5px solid #d1d5db;
      border-radius: 8px;
      padding: 0.5rem 0.65rem;
      outline: none;
      text-transform: none;
      letter-spacing: normal;
      font-size: 0.88rem;
    }
    .field input:focus {
      border-color: #4f46e5;
    }
    .row-actions {
      display: flex;
      gap: 0.45rem;
      flex-wrap: wrap;
    }
    .btn-primary,
    .btn-ghost,
    .btn-danger {
      border-radius: 8px;
      padding: 0.45rem 0.8rem;
      font-size: 0.82rem;
      font-weight: 600;
      border: 1px solid transparent;
      cursor: pointer;
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
    .btn-danger {
      background: white;
      border-color: #fecaca;
      color: #dc2626;
    }
    .list-wrap {
      display: flex;
      flex-direction: column;
      gap: 0.55rem;
      max-height: 260px;
      overflow: auto;
      border-top: 1px solid #f3f4f6;
      padding-top: 0.7rem;
    }
    .list-item {
      border: 1px solid #e5e7eb;
      border-radius: 8px;
      padding: 0.65rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 0.6rem;
    }
    .list-item p {
      margin: 0.15rem 0 0;
      font-size: 0.76rem;
      color: #6b7280;
    }
    .global-error {
      background: #fef2f2;
      border: 1px solid #fecaca;
      color: #dc2626;
      padding: 0.65rem 0.85rem;
      border-radius: 8px;
      margin-bottom: 1rem;
      font-size: 0.85rem;
    }
  `],
})
export class AdminManagementComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly adminApi = inject(AdminApiService);

  readonly recipients = signal<Recipient[]>([]);
  readonly drivers = signal<Driver[]>([]);
  readonly recipientSaving = signal(false);
  readonly driverSaving = signal(false);
  readonly error = signal<string | null>(null);
  readonly editingRecipientId = signal<string | null>(null);

  readonly recipientForm = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(255)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
    phone: ['', [Validators.required, Validators.maxLength(50)]],
    address: ['', [Validators.required, Validators.maxLength(255)]],
    documentNumber: ['', [Validators.required, Validators.maxLength(50)]],
  });

  readonly driverForm = this.fb.group({
    username: ['', [Validators.required, Validators.maxLength(100)]],
    password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(255)]],
  });

  ngOnInit(): void {
    this.reloadRecipients();
    this.reloadDrivers();
  }

  saveRecipient(): void {
    if (this.recipientForm.invalid) {
      this.recipientForm.markAllAsTouched();
      return;
    }

    this.recipientSaving.set(true);
    this.error.set(null);

    const payload = this.recipientForm.getRawValue() as UpsertRecipientRequest;
    const id = this.editingRecipientId();
    const request$ = id
      ? this.adminApi.updateRecipient(id, payload)
      : this.adminApi.createRecipient(payload);

    request$.subscribe({
      next: () => {
        this.recipientSaving.set(false);
        this.editingRecipientId.set(null);
        this.recipientForm.reset();
        this.reloadRecipients();
      },
      error: (err: unknown) => {
        this.recipientSaving.set(false);
        this.error.set(getApiErrorMessage(err, 'Failed to save recipient'));
      },
    });
  }

  editRecipient(recipient: Recipient): void {
    this.editingRecipientId.set(recipient.id);
    this.recipientForm.patchValue({
      name: recipient.name,
      email: recipient.email,
      phone: recipient.phone,
      address: recipient.address,
      documentNumber: recipient.documentNumber,
    });
  }

  cancelRecipientEdit(): void {
    this.editingRecipientId.set(null);
    this.recipientForm.reset();
  }

  deleteRecipient(recipientId: string): void {
    this.error.set(null);
    this.adminApi.deleteRecipient(recipientId).subscribe({
      next: () => this.reloadRecipients(),
      error: (err: unknown) => this.error.set(getApiErrorMessage(err, 'Failed to delete recipient')),
    });
  }

  createDriver(): void {
    if (this.driverForm.invalid) {
      this.driverForm.markAllAsTouched();
      return;
    }

    this.driverSaving.set(true);
    this.error.set(null);

    const payload = this.driverForm.getRawValue() as CreateDriverRequest;
    this.adminApi.createDriver(payload).subscribe({
      next: () => {
        this.driverSaving.set(false);
        this.driverForm.reset();
        this.reloadDrivers();
      },
      error: (err: unknown) => {
        this.driverSaving.set(false);
        this.error.set(getApiErrorMessage(err, 'Failed to create driver'));
      },
    });
  }

  deleteDriver(driverId: string): void {
    this.error.set(null);
    this.adminApi.deleteDriver(driverId).subscribe({
      next: () => this.reloadDrivers(),
      error: (err: unknown) => this.error.set(getApiErrorMessage(err, 'Failed to delete driver')),
    });
  }

  private reloadRecipients(): void {
    this.adminApi.getRecipients().subscribe({
      next: (items) => this.recipients.set(items),
      error: (err: unknown) => this.error.set(getApiErrorMessage(err, 'Failed to load recipients')),
    });
  }

  private reloadDrivers(): void {
    this.adminApi.getDrivers().subscribe({
      next: (items) => this.drivers.set(items),
      error: (err: unknown) => this.error.set(getApiErrorMessage(err, 'Failed to load drivers')),
    });
  }
}
