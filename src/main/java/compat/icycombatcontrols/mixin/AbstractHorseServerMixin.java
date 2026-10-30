package compat.icycombatcontrols.mixin;

import compat.icycombatcontrols.CombatMode;
import compat.icycombatcontrols.ThrottleMath;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractHorse.class, priority = 2000)
public abstract class AbstractHorseServerMixin extends LivingEntity {
    @Shadow protected float playerJumpPendingScale;
    @Shadow protected boolean allowStandSliding;
    @Shadow public abstract boolean isStanding();

    @Unique private double icyCombatControls$throttle;

    protected AbstractHorseServerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "getRiddenRotation", at = @At("HEAD"), cancellable = true)
    private void icyCombatControls$useCombatRotation(
            LivingEntity rider, CallbackInfoReturnable<Vec2> cir) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        if (!(rider instanceof Player player) || !CombatMode.active(horse, player)) {
            return;
        }

        cir.setReturnValue(new Vec2(
                rider.getXRot() * 0.5F,
                getYRot() + ThrottleMath.turnDelta(icyCombatControls$throttle, rider.xxa * 0.5F)));
    }

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    private void icyCombatControls$useCombatThrottle(
            Player rider, Vec3 travelVector, CallbackInfoReturnable<Vec3> cir) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        if (!CombatMode.active(horse, rider)) {
            icyCombatControls$throttle = 0.0D;
            return;
        }

        CombatMode.clearBetterHorsesGait(horse);
        if (onGround() && playerJumpPendingScale == 0.0F && isStanding() && !allowStandSliding) {
            cir.setReturnValue(Vec3.ZERO);
            return;
        }

        icyCombatControls$throttle = ThrottleMath.update(icyCombatControls$throttle, rider.zza);
        cir.setReturnValue(new Vec3(0.0D, 0.0D, icyCombatControls$throttle));
    }
}
