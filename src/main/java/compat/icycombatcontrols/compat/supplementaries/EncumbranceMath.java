package compat.icycombatcontrols.compat.supplementaries;

import java.util.OptionalInt;

public final class EncumbranceMath {
    private EncumbranceMath() {
    }

    public static OptionalInt amplifier(float carriedWeight, int increment) {
        if (increment <= 0 || carriedWeight <= increment) {
            return OptionalInt.empty();
        }

        int amplifier = ((((int) carriedWeight) - 1) / increment) - 1;
        return OptionalInt.of(amplifier);
    }
}
