import { describe, it, expect } from 'vitest';
import {
  isValidTransition,
  VALID_TRANSITIONS,
  PackageStatus,
} from './package.model';

describe('Package Model', () => {
  describe('isValidTransition', () => {
    it('should allow RECEIVED → IN_TRANSIT', () => {
      expect(isValidTransition('RECEIVED', 'IN_TRANSIT')).toBe(true);
    });

    it('should allow IN_TRANSIT → DELIVERED', () => {
      expect(isValidTransition('IN_TRANSIT', 'DELIVERED')).toBe(true);
    });

    it('should reject RECEIVED → DELIVERED (invalid business rule)', () => {
      expect(isValidTransition('RECEIVED', 'DELIVERED')).toBe(false);
    });

    it('should reject DELIVERED → any status', () => {
      expect(isValidTransition('DELIVERED', 'RECEIVED')).toBe(false);
      expect(isValidTransition('DELIVERED', 'IN_TRANSIT')).toBe(false);
    });

    it('should reject IN_TRANSIT → RECEIVED (backward move)', () => {
      expect(isValidTransition('IN_TRANSIT', 'RECEIVED')).toBe(false);
    });
  });

  describe('VALID_TRANSITIONS', () => {
    it('RECEIVED can only go to IN_TRANSIT', () => {
      expect(VALID_TRANSITIONS.RECEIVED).toEqual(['IN_TRANSIT']);
    });

    it('IN_TRANSIT can only go to DELIVERED', () => {
      expect(VALID_TRANSITIONS.IN_TRANSIT).toEqual(['DELIVERED']);
    });

    it('DELIVERED has no valid next states', () => {
      expect(VALID_TRANSITIONS.DELIVERED).toEqual([]);
    });
  });
});
