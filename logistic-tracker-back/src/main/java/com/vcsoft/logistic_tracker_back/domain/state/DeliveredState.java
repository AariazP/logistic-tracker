package com.vcsoft.logistic_tracker_back.domain.state;

import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

/**
 * State: DELIVERED — terminal state.
 * No further transitions are permitted.
 */
public class DeliveredState implements PackageState {

    @Override
    public PackageState transition(PackageStatus next) {
        throw new InvalidStateTransitionException(getStatus(), next);
    }

    @Override
    public PackageStatus getStatus() {
        return PackageStatus.DELIVERED;
    }
}
