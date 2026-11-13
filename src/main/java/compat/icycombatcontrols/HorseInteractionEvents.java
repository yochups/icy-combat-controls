package compat.icycombatcontrols;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityMountEvent;

public final class HorseInteractionEvents {
    private HorseInteractionEvents() {
    }

    public static void onMount(EntityMountEvent event) {
        if (event.isMounting() && event.getEntityMounting() instanceof AbstractHorse) {
            event.setCanceled(true);
            return;
        }

        if (!(event.getEntityMounting() instanceof Player player)
                || !(event.getEntityBeingMounted() instanceof AbstractHorse horse)) {
            return;
        }

        if (IcyCombatControlsConfig.RESET_THROTTLE_ON_MOUNT_OR_DISMOUNT.get()) {
            HorseThrottleAccess.reset(horse);
        }

        if (event.isDismounting()) {
            player.setDeltaMovement(Vec3.ZERO);
            player.hurtMarked = true;
        }
    }
}
