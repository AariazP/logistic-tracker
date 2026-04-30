package com.vcsoft.logistic_tracker_back.domain.state;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

/**
 * State pattern interface. Each concrete state knows which transitions are
 * allowed from it, making the machine easy to extend: add a new state class,
 * register it in PackageStateContext — done.
 */
public interface PackageState {

    /**
     * Attempts to transition to the requested next status.
     *
     * @param next the target status
     * @return the new PackageState after a valid transition
     * @throws com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException
     *         if the transition is forbidden
     */
    PackageState transition(PackageStatus next);

    /**
     * @return the status this state represents
     */
    PackageStatus getStatus();
}
