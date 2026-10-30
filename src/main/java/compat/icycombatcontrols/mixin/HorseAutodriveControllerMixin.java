package compat.icycombatcontrols.mixin;

import compat.icycombatcontrols.CombatMode;
import icy.betterhorses.net.client.HorseAutodriveController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HorseAutodriveController.class, priority = 2000, remap = false)
public abstract class HorseAutodriveControllerMixin {
    @Shadow public abstract void reset();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void icyCombatControls$disableAutodrive(
            long tick,
            boolean eligible,
            int horseId,
            boolean forwardDown,
            boolean backDown,
            boolean leftDown,
            boolean rightDown,
            float forwardImpulse,
            float leftImpulse,
            CallbackInfoReturnable<HorseAutodriveController.Output> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        Entity vehicle = player == null ? null : player.getControlledVehicle();
        if (!(vehicle instanceof AbstractHorse horse) || !CombatMode.active(horse, player)) {
            return;
        }

        reset();
        cir.setReturnValue(new HorseAutodriveController.Output(
                false,
                forwardDown,
                backDown,
                leftDown,
                rightDown,
                forwardImpulse,
                leftImpulse));
    }
}
