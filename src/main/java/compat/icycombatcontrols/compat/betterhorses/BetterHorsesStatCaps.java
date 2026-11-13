package compat.icycombatcontrols.compat.betterhorses;

import icy.betterhorses.net.IHorseData;
import icy.betterhorses.net.api.HorseFeaturesEvent;
import icy.betterhorses.net.feature.HorseFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.util.UUID;

public final class BetterHorsesStatCaps implements HorseFeature {
    private static final UUID CAP_ID = UUID.fromString("b68cdb45-a186-47b2-a149-c02c0993be57");

    public static void addHorseFeature(HorseFeaturesEvent event) {
        event.add(new BetterHorsesStatCaps());
    }

    @Override
    public void onLoad(AbstractHorse horse, IHorseData data) {
        apply(horse, data);
    }

    @Override
    public void tick(AbstractHorse horse, IHorseData data) {
        apply(horse, data);
    }

    @Override
    public void onRemoved(AbstractHorse horse, IHorseData data) {
        remove(horse, Attributes.MAX_HEALTH);
        remove(horse, Attributes.MOVEMENT_SPEED);
        remove(horse, Attributes.JUMP_STRENGTH);
    }

    private static void apply(AbstractHorse horse, IHorseData data) {
        ResourceLocation breed = data.bh_getBreedKey() == null ? null : data.bh_getBreedKey().location();
        BetterHorsesStatCapsConfig.Limits limits = BetterHorsesStatCapsConfig.forBreed(breed);
        cap(horse, Attributes.MAX_HEALTH, limits.health().max());
        cap(horse, Attributes.MOVEMENT_SPEED, limits.speed().max());
        cap(horse, Attributes.JUMP_STRENGTH, limits.jump().max());
        if (!horse.level().isClientSide() && horse.getHealth() > horse.getMaxHealth()) {
            horse.setHealth(horse.getMaxHealth());
        }
    }

    private static void cap(AbstractHorse horse, Attribute attribute, double maximum) {
        AttributeInstance instance = horse.getAttribute(attribute);
        if (instance == null) return;
        AttributeModifier old = instance.getModifier(CAP_ID);
        double amount = StatRangeMath.capModifier(instance.getValue(), old == null ? 0.0D : old.getAmount(), maximum);
        if (old != null && Math.abs(old.getAmount() - amount) < 1.0E-10D) return;
        if (old != null) instance.removeModifier(CAP_ID);
        if (amount != 0.0D) {
            instance.addTransientModifier(new AttributeModifier(CAP_ID, "icy_combat_controls:stat_cap",
                    amount, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private static void remove(AbstractHorse horse, Attribute attribute) {
        AttributeInstance instance = horse.getAttribute(attribute);
        if (instance != null) instance.removeModifier(CAP_ID);
    }
}
