package compat.icycombatcontrols.mixin;

import compat.icycombatcontrols.CombatMode;
import icy.betterhorses.net.client.HorseGearController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HorseGearController.class, priority = 2000, remap = false)
public abstract class HorseGearControllerMixin {
    @Shadow public abstract void reset();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void icyCombatControls$disableGaits(
            boolean eligible,
            AbstractHorse horse,
            boolean forwardDown,
            boolean backDown,
            CallbackInfoReturnable<HorseGearController.Output> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || horse == null || !CombatMode.active(horse, player)) {
            return;
        }

        reset();
        CombatMode.clearBetterHorsesGait(horse);
        cir.setReturnValue(HorseGearController.Output.PASS_THROUGH);
    }
}
