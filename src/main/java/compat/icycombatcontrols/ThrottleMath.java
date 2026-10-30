package compat.icycombatcontrols;

public final class ThrottleMath {
    private static final double FORWARD_LIMIT = 1.0D;
    private static final double REVERSE_LIMIT = -0.25D;
    private static final double STEP = 0.05D;

    private ThrottleMath() {
    }

    public static double update(double throttle, float forwardInput) {
        if (forwardInput > 0.0F && throttle < FORWARD_LIMIT) {
            throttle = Math.min(FORWARD_LIMIT, throttle + STEP);
        } else if (forwardInput < 0.0F && throttle > REVERSE_LIMIT) {
            throttle = Math.max(REVERSE_LIMIT, throttle - STEP);
        }

        return Math.abs(throttle) < STEP ? throttle * 0.95D : throttle;
    }

    public static float turnDelta(double throttle, float strafeInput) {
        if (strafeInput == 0.0F) {
            return 0.0F;
        }
        double degrees = Math.min(Math.toDegrees(Math.atan(STEP / Math.abs(throttle))), 10.0D);
        return (float) (degrees * (strafeInput < 0.0F ? 1.0D : -1.0D));
    }
}
