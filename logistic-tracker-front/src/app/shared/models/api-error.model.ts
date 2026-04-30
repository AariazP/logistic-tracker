import { HttpErrorResponse } from '@angular/common/http';

export interface ApiError {
  status: number;
  error: string;
  message: string;
  timestamp: string;
  fieldErrors?: Record<string, string> | null;
}

function isApiError(payload: unknown): payload is ApiError {
  if (!payload || typeof payload !== 'object') {
    return false;
  }

  const candidate = payload as Partial<ApiError>;
  return typeof candidate.message === 'string';
}

export function getApiErrorMessage(error: unknown, fallbackMessage: string): string {
  if (error instanceof HttpErrorResponse) {
    const payload = error.error;

    if (isApiError(payload)) {
      const fieldErrorMessage = payload.fieldErrors
        ? Object.values(payload.fieldErrors)[0]
        : null;
      return fieldErrorMessage ?? payload.message;
    }

    if (typeof payload === 'string' && payload.trim().length > 0) {
      return payload;
    }

    const payloadMessage =
      payload && typeof payload === 'object' && 'message' in payload && typeof payload.message === 'string'
        ? payload.message
        : null;

    if (payloadMessage) {
      return payloadMessage;
    }

    if (error.status === 401) {
      return 'Unauthorized. Please sign in again.';
    }

    if (error.status === 403) {
      return 'You do not have permission to perform this action.';
    }
  }

  if (error instanceof Error && error.message) {
    return error.message;
  }

  return fallbackMessage;
}
