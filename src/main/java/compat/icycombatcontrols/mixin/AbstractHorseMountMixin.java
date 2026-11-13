package compat.icycombatcontrols.mixin;

import compat.icycombatcontrols.IcyCombatControlsConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractHorse.class, priority = 2000)
public abstract class AbstractHorseMountMixin extends LivingEntity {
    protected AbstractHorseMountMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = "doPlayerRide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;setStanding(Z)V"))
    private void icyCombatControls$faceHorseDirection(Player player, CallbackInfo ci) {
        if (level().isClientSide()
                || !IcyCombatControlsConfig.FACE_HORSE_DIRECTION_ON_MOUNT.get()
                || ModList.get().isLoaded("immersive_horse_riding")) {
            return;
        }

        player.setYRot(getYRot());
        player.yRotO = yRotO;
    }
}
