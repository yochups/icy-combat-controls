package compat.icycombatcontrols.mixin;

import compat.icycombatcontrols.HorseThrottleAccess;
import compat.icycombatcontrols.IcyCombatControlsConfig;
import icy.betterhorses.net.BhHorseKind;
import icy.betterhorses.net.IcysBetterHorsesClientForgeEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IcysBetterHorsesClientForgeEvents.class, priority = 2000, remap = false)
public abstract class BetterHorsesRearClientMixin {
    @Inject(
            method = "tryRear",
            at = @At(
                    value = "INVOKE",
                    target = "Licy/betterhorses/net/BhNetworking;sendToServer(Ljava/lang/Object;)V",
                    remap = false))
    private static void icyCombatControls$resetThrottleOnRear(Minecraft minecraft, CallbackInfo ci) {
        LocalPlayer player = minecraft.player;
        if (IcyCombatControlsConfig.RESET_THROTTLE_ON_REAR.get()
                && player != null
                && BhHorseKind.managed(player.getVehicle())
                && player.getVehicle() instanceof AbstractHorse horse) {
            HorseThrottleAccess.reset(horse);
        }
    }
}
