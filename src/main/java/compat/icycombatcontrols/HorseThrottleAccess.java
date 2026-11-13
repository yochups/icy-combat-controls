package compat.icycombatcontrols;

import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.lang.reflect.Field;

public interface HorseThrottleAccess {
    void icyCombatControls$resetThrottle();

    static void reset(AbstractHorse horse) {
        if (horse instanceof HorseThrottleAccess throttle) {
            throttle.icyCombatControls$resetThrottle();
        }
        // HCC injects its own private throttle field at runtime, so it cannot be accessed normally here.
        try {
            HccThrottle.FIELD.setDouble(horse, 0.0D);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not reset Horse Combat Controls throttle", e);
        }
    }

    final class HccThrottle {
        private static final Field FIELD = find();

        private HccThrottle() {
        }

        private static Field find() {
            try {
                Field field = AbstractHorse.class.getDeclaredField("horseCombatControls$prevSpeedPercent");
                field.setAccessible(true);
                return field;
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Horse Combat Controls throttle field was not found", e);
            }
        }
    }
}
