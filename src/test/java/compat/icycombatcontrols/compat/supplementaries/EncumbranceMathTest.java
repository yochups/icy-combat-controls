package compat.icycombatcontrols.compat.supplementaries;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncumbranceMathTest {
    @Test
    void matchesSupplementariesThresholdsAndAmplifiers() {
        assertEquals(OptionalInt.empty(), EncumbranceMath.amplifier(2.0F, 2));
        assertEquals(OptionalInt.of(0), EncumbranceMath.amplifier(3.0F, 2));
        assertEquals(OptionalInt.of(1), EncumbranceMath.amplifier(5.0F, 2));
        assertEquals(OptionalInt.of(2), EncumbranceMath.amplifier(7.0F, 2));
        assertEquals(OptionalInt.of(-1), EncumbranceMath.amplifier(2.5F, 2));
    }

    @Test
    void safelyDisablesInvalidZeroIncrement() {
        assertEquals(OptionalInt.empty(), EncumbranceMath.amplifier(3.0F, 0));
    }
}
