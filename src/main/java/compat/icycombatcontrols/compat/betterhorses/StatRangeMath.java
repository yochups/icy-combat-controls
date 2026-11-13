package compat.icycombatcontrols.compat.betterhorses;

public final class StatRangeMath {
    private StatRangeMath() {}

    public static double remap(double value, double oldMin, double oldMax, double newMin, double newMax) {
        double fraction = oldMax == oldMin ? 0.5D : (value - oldMin) / (oldMax - oldMin);
        return newMin + Math.max(0.0D, Math.min(1.0D, fraction)) * (newMax - newMin);
    }

    static double capModifier(double effective, double previousModifier, double maximum) {
        double uncapped = effective / (1.0D + previousModifier);
        return uncapped > maximum && uncapped > 0.0D ? maximum / uncapped - 1.0D : 0.0D;
    }
}
