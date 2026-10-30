package compat.icycombatcontrols.compat.supplementaries;

import compat.icycombatcontrols.IcyCombatControlsConfig;
import icy.betterhorses.net.IHorseData;
import icy.betterhorses.net.feature.HorseFeature;
import net.mehvahdjukaar.supplementaries.common.items.SackItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.util.OptionalInt;

final class SupplementariesHorseEncumbrance implements HorseFeature {
    private static final int CHECK_INTERVAL_TICKS = 27;
    private static final int EFFECT_DURATION_TICKS = 20 * 10;

    @Override
    public void tick(AbstractHorse horse, IHorseData data) {
        if (horse.level().isClientSide
                || horse.level().getGameTime() % CHECK_INTERVAL_TICKS != 0L
                || !IcyCombatControlsConfig.HORSE_ENCUMBRANCE.get()
                || !CommonConfigs.Functional.SACK_ENABLED.get()
                || !CommonConfigs.Functional.SACK_PENALTY.get()
                || (IcyCombatControlsConfig.ONLY_WHILE_RIDDEN.get() && !horse.isVehicle())) {
            return;
        }

        float carriedWeight = 0.0F;
        if (data.bh_hasChestGear() && !data.bh_hasEnderChestGear()) {
            carriedWeight += weightOf(data.bh_getChestContainer());
        }
        if (IcyCombatControlsConfig.INCLUDE_CART_STORAGE.get() && data.bh_hasCartChest()) {
            carriedWeight += weightOf(data.bh_getCartChestContainer());
        }

        carriedWeight *= IcyCombatControlsConfig.HORSE_ENCUMBRANCE_MULTIPLIER.get().floatValue();
        OptionalInt amplifier = EncumbranceMath.amplifier(
                carriedWeight,
                CommonConfigs.Functional.SACK_INCREMENT.get());
        if (amplifier.isPresent()) {
            horse.addEffect(new MobEffectInstance(
                    ModRegistry.OVERENCUMBERED.get(),
                    EFFECT_DURATION_TICKS,
                    amplifier.getAsInt(),
                    false,
                    false,
                    true));
        }
    }

    private static float weightOf(SimpleContainer inventory) {
        float weight = 0.0F;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            weight += SackItem.getEncumber(inventory.getItem(slot));
        }
        return weight;
    }
}
