package com.vcsoft.logistic_tracker_back.domain.state;

import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("State Pattern Tests")
class PackageStateTest {

    @Test
    @DisplayName("ReceivedState → IN_TRANSIT returns InTransitState")
    void receivedToInTransit() {
        PackageState state = new ReceivedState();
        PackageState next = state.transition(PackageStatus.IN_TRANSIT);
        assertThat(next).isInstanceOf(InTransitState.class);
        assertThat(next.getStatus()).isEqualTo(PackageStatus.IN_TRANSIT);
    }

    @Test
    @DisplayName("ReceivedState → DELIVERED throws exception")
    void receivedToDeliveredThrows() {
        PackageState state = new ReceivedState();
        assertThatThrownBy(() -> state.transition(PackageStatus.DELIVERED))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    @Test
    @DisplayName("InTransitState → DELIVERED returns DeliveredState")
    void inTransitToDelivered() {
        PackageState state = new InTransitState();
        PackageState next = state.transition(PackageStatus.DELIVERED);
        assertThat(next).isInstanceOf(DeliveredState.class);
        assertThat(next.getStatus()).isEqualTo(PackageStatus.DELIVERED);
    }

    @Test
    @DisplayName("InTransitState → RECEIVED throws exception")
    void inTransitToReceivedThrows() {
        PackageState state = new InTransitState();
        assertThatThrownBy(() -> state.transition(PackageStatus.RECEIVED))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    @Test
    @DisplayName("DeliveredState is terminal — all transitions throw")
    void deliveredIsTerminal() {
        PackageState state = new DeliveredState();
        for (PackageStatus s : PackageStatus.values()) {
            assertThatThrownBy(() -> state.transition(s))
                    .isInstanceOf(InvalidStateTransitionException.class);
        }
    }

    @Test
    @DisplayName("PackageStateFactory restores correct state for each status")
    void factoryRestoresCorrectState() {
        assertThat(PackageStateFactory.from(PackageStatus.RECEIVED)).isInstanceOf(ReceivedState.class);
        assertThat(PackageStateFactory.from(PackageStatus.IN_TRANSIT)).isInstanceOf(InTransitState.class);
        assertThat(PackageStateFactory.from(PackageStatus.DELIVERED)).isInstanceOf(DeliveredState.class);
    }
}
