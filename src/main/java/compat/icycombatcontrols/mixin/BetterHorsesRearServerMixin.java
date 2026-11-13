package compat.icycombatcontrols.mixin;

import compat.icycombatcontrols.HorseThrottleAccess;
import compat.icycombatcontrols.IcyCombatControlsConfig;
import icy.betterhorses.net.IcysBetterHorses;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IcysBetterHorses.class, priority = 2000, remap = false)
public abstract class BetterHorsesRearServerMixin {
    @Inject(method = "handleRear", at = @At("HEAD"))
    private static void icyCombatControls$resetThrottleOnRear(
            ServerPlayer player, int horseId, CallbackInfo ci) {
        if (IcyCombatControlsConfig.RESET_THROTTLE_ON_REAR.get()
                && player.getVehicle() instanceof AbstractHorse horse
                && horse.getId() == horseId) {
            HorseThrottleAccess.reset(horse);
        }
    }
}
