package compat.icycombatcontrols.compat.betterhorses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatRangeMathTest {
    @Test
    void spawnRollKeepsItsRelativePositionInTheConfiguredRange() {
        assertEquals(0.75D, StatRangeMath.remap(0.7D, 0.4D, 1.0D, 0.5D, 1.0D), 1.0E-12D);
        assertEquals(0.5D, StatRangeMath.remap(0.3D, 0.4D, 1.0D, 0.5D, 1.0D), 1.0E-12D);
        assertEquals(1.0D, StatRangeMath.remap(1.1D, 0.4D, 1.0D, 0.5D, 1.0D), 1.0E-12D);
    }

    @Test
    void existingCapCanBeRecomputedWithoutCompounding() {
        double initial = StatRangeMath.capModifier(1.75D, 0.0D, 1.0D);
        assertEquals(1.0D, 1.75D * (1.0D + initial), 1.0E-12D);
        assertEquals(initial, StatRangeMath.capModifier(1.0D, initial, 1.0D), 1.0E-12D);
        assertEquals(0.0D, StatRangeMath.capModifier(1.0D, initial, 2.0D), 1.0E-12D);
    }
}
