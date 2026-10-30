package compat.icycombatcontrols.compat.supplementaries;

import icy.betterhorses.net.api.HorseFeaturesEvent;

public final class SupplementariesCompat {
    private SupplementariesCompat() {
    }

    public static void addHorseFeature(HorseFeaturesEvent event) {
        event.add(new SupplementariesHorseEncumbrance());
    }
}
