export interface Recipient {
  id: string;
  name: string;
  email: string;
  phone: string;
  address: string;
  documentNumber: string;
  createdAt: string;
  updatedAt: string;
}

export interface UpsertRecipientRequest {
  name: string;
  email: string;
  phone: string;
  address: string;
  documentNumber: string;
}
