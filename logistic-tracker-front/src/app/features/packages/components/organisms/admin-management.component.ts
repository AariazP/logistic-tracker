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
  templateUrl: './admin-management.component.html',
  styleUrl: './admin-management.component.css',
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
