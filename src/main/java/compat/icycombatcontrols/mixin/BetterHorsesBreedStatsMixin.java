package compat.icycombatcontrols.mixin;

import compat.icycombatcontrols.compat.betterhorses.BetterHorsesStatCapsConfig;
import compat.icycombatcontrols.compat.betterhorses.StatRangeMath;
import icy.betterhorses.net.BhBreedData;
import icy.betterhorses.net.entity.BhBreedHorse;
import icy.betterhorses.net.registry.ArchetypeType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BhBreedHorse.class, remap = false)
public abstract class BetterHorsesBreedStatsMixin {
    @Inject(method = "bhRollStats", at = @At("TAIL"))
    private void icyCombatControls$rollConfiguredStats(CallbackInfo ci) {
        BhBreedHorse horse = (BhBreedHorse) (Object) this;
        ArchetypeType archetype = BhBreedData.of(horse.bhFixedBreed()).archetype();
        BetterHorsesStatCapsConfig.Limits limits = icyCombatControls$limits();
        icyCombatControls$remap(horse, Attributes.MAX_HEALTH,
                archetype.lowHealth(), archetype.highHealth(), limits.health(), true);
        icyCombatControls$remap(horse, Attributes.MOVEMENT_SPEED,
                archetype.lowSpeed(), archetype.highSpeed(), limits.speed(), false);
        icyCombatControls$remap(horse, Attributes.JUMP_STRENGTH,
                archetype.lowJump(), archetype.highJump(), limits.jump(), false);
        horse.setHealth(horse.getMaxHealth());
    }

    @Redirect(method = {"bhInheritStats", "bhConvertFrom"},
            at = @At(value = "INVOKE", target = "Licy/betterhorses/net/registry/ArchetypeType;clampHealth(D)D"))
    private double icyCombatControls$clampHealth(ArchetypeType archetype, double value) {
        BetterHorsesStatCapsConfig.Range range = icyCombatControls$limits().health();
        return Math.round(icyCombatControls$clamp(value, range));
    }

    @Redirect(method = {"bhInheritStats", "bhConvertFrom"},
            at = @At(value = "INVOKE", target = "Licy/betterhorses/net/registry/ArchetypeType;clampSpeed(D)D"))
    private double icyCombatControls$clampSpeed(ArchetypeType archetype, double value) {
        return icyCombatControls$clamp(value, icyCombatControls$limits().speed());
    }

    @Redirect(method = {"bhInheritStats", "bhConvertFrom"},
            at = @At(value = "INVOKE", target = "Licy/betterhorses/net/registry/ArchetypeType;clampJump(D)D"))
    private double icyCombatControls$clampJump(ArchetypeType archetype, double value) {
        return icyCombatControls$clamp(value, icyCombatControls$limits().jump());
    }

    @Unique
    private BetterHorsesStatCapsConfig.Limits icyCombatControls$limits() {
        BhBreedHorse horse = (BhBreedHorse) (Object) this;
        return BetterHorsesStatCapsConfig.forBreed(horse.bhFixedBreed().location());
    }

    @Unique
    private static void icyCombatControls$remap(BhBreedHorse horse, Attribute attribute,
                                                double oldMin, double oldMax,
                                                BetterHorsesStatCapsConfig.Range range, boolean round) {
        AttributeInstance instance = horse.getAttribute(attribute);
        if (instance == null) return;
        double value = StatRangeMath.remap(instance.getBaseValue(), oldMin, oldMax, range.min(), range.max());
        instance.setBaseValue(round ? Math.round(value) : value);
    }

    @Unique
    private static double icyCombatControls$clamp(double value, BetterHorsesStatCapsConfig.Range range) {
        return Math.max(range.min(), Math.min(range.max(), value));
    }
}
