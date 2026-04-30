package com.vcsoft.logistic_tracker_back.domain.state;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

/**
 * Factory / resolver: maps a persisted PackageStatus enum value back to the
 * correct PackageState instance. Called when loading an entity from the DB so
 * the aggregate root is always in a valid state.
 */
public final class PackageStateFactory {

    private PackageStateFactory() {}

    public static PackageState from(PackageStatus status) {
        return switch (status) {
            case RECEIVED -> new ReceivedState();
            case IN_TRANSIT -> new InTransitState();
            case DELIVERED -> new DeliveredState();
        };
    }
}
