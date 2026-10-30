package compat.icycombatcontrols;

import github.pitbox46.horsecombatcontrols.HorseCombatControls;
import icy.betterhorses.net.BhHorseKind;
import icy.betterhorses.net.IHorseData;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

public final class CombatMode {
    private CombatMode() {
    }

    public static boolean active(AbstractHorse horse, Player rider) {
        return BhHorseKind.managed(horse) && HorseCombatControls.isInCombatMode(rider);
    }

    public static void clearBetterHorsesGait(AbstractHorse horse) {
        IHorseData data = IHorseData.of(horse);
        if (data.bh_getGear() != 0) {
            data.bh_setGear(0);
        }
        if (data.bh_getGaitGear() != 0) {
            data.bh_setGaitGear(0);
        }
    }
}
