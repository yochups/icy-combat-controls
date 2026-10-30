package compat.icycombatcontrols;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThrottleMathTest {
    private static final double EPSILON = 1.0E-9D;

    @Test
    void throttleUsesHorseCombatControlsLimits() {
        double throttle = 0.0D;
        for (int i = 0; i < 40; i++) {
            throttle = ThrottleMath.update(throttle, 1.0F);
        }
        assertEquals(1.0D, throttle, EPSILON);

        for (int i = 0; i < 40; i++) {
            throttle = ThrottleMath.update(throttle, -1.0F);
        }
        assertEquals(-0.25D, throttle, EPSILON);
    }

    @Test
    void releasingMovementKeepsTheSelectedThrottle() {
        assertEquals(0.6D, ThrottleMath.update(0.6D, 0.0F), EPSILON);
        assertEquals(0.019D, ThrottleMath.update(0.02D, 0.0F), EPSILON);
    }

    @Test
    void steeringIsCappedAndDirectionMatchesUpstream() {
        assertEquals(-10.0F, ThrottleMath.turnDelta(0.0D, 0.5F), 0.0001F);
        assertEquals(10.0F, ThrottleMath.turnDelta(0.0D, -0.5F), 0.0001F);
        assertEquals(0.0F, ThrottleMath.turnDelta(1.0D, 0.0F), 0.0001F);
    }
}
