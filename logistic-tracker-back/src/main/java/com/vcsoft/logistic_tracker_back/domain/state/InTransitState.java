package com.vcsoft.logistic_tracker_back.domain.state;

import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

/**
 * State: IN_TRANSIT.
 * Only allowed transition: IN_TRANSIT → DELIVERED.
 */
public class InTransitState implements PackageState {

    @Override
    public PackageState transition(PackageStatus next) {
        if (next == PackageStatus.DELIVERED) {
            return new DeliveredState();
        }
        throw new InvalidStateTransitionException(getStatus(), next);
    }

    @Override
    public PackageStatus getStatus() {
        return PackageStatus.IN_TRANSIT;
    }
}
