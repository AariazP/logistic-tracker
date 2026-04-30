package com.vcsoft.logistic_tracker_back.domain.state;

import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

/**
 * State: RECEIVED.
 * Only allowed transition: RECEIVED → IN_TRANSIT.
 */
public class ReceivedState implements PackageState {

    @Override
    public PackageState transition(PackageStatus next) {
        if (next == PackageStatus.IN_TRANSIT) {
            return new InTransitState();
        }
        throw new InvalidStateTransitionException(getStatus(), next);
    }

    @Override
    public PackageStatus getStatus() {
        return PackageStatus.RECEIVED;
    }
}
