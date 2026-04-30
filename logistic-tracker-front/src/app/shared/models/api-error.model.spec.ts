import { HttpErrorResponse } from '@angular/common/http';
import { describe, it, expect } from 'vitest';
import { getApiErrorMessage } from './api-error.model';

describe('getApiErrorMessage', () => {
  it('returns first field error when API payload includes fieldErrors', () => {
    const error = new HttpErrorResponse({
      status: 400,
      error: {
        message: 'Validation failed',
        timestamp: '2026-01-01T00:00:00Z',
        status: 400,
        error: 'Bad Request',
        fieldErrors: {
          recipientId: 'Recipient is required',
          weight: 'Weight must be positive',
        },
      },
    });

    expect(getApiErrorMessage(error, 'Fallback')).toBe('Recipient is required');
  });

  it('returns API payload message when no fieldErrors are present', () => {
    const error = new HttpErrorResponse({
      status: 400,
      error: {
        message: 'Validation failed',
        timestamp: '2026-01-01T00:00:00Z',
        status: 400,
        error: 'Bad Request',
      },
    });

    expect(getApiErrorMessage(error, 'Fallback')).toBe('Validation failed');
  });

  it('returns string payload when backend responds with plain text', () => {
    const error = new HttpErrorResponse({
      status: 500,
      error: 'Server exploded',
    });

    expect(getApiErrorMessage(error, 'Fallback')).toBe('Server exploded');
  });

  it('returns payload.message when payload is an object without full API error shape', () => {
    const error = new HttpErrorResponse({
      status: 500,
      error: { message: 'Unexpected exception' },
    });

    expect(getApiErrorMessage(error, 'Fallback')).toBe('Unexpected exception');
  });

  it('returns unauthorized message for 401 errors without payload message', () => {
    const error = new HttpErrorResponse({
      status: 401,
      error: '',
    });

    expect(getApiErrorMessage(error, 'Fallback')).toBe('Unauthorized. Please sign in again.');
  });

  it('returns forbidden message for 403 errors without payload message', () => {
    const error = new HttpErrorResponse({
      status: 403,
      error: '',
    });

    expect(getApiErrorMessage(error, 'Fallback')).toBe('You do not have permission to perform this action.');
  });

  it('returns Error.message for generic JavaScript errors', () => {
    const error = new Error('Boom');

    expect(getApiErrorMessage(error, 'Fallback')).toBe('Boom');
  });

  it('returns fallback message for unknown error values', () => {
    expect(getApiErrorMessage(undefined, 'Fallback')).toBe('Fallback');
  });
});
