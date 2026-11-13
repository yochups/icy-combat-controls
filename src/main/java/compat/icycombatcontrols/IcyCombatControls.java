package compat.icycombatcontrols;

import compat.icycombatcontrols.compat.supplementaries.SupplementariesCompat;
import compat.icycombatcontrols.compat.betterhorses.BetterHorsesStatCaps;
import compat.icycombatcontrols.compat.betterhorses.BetterHorsesStatCapsConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(IcyCombatControls.MOD_ID)
public final class IcyCombatControls {
    public static final String MOD_ID = "icy_combat_controls";

    public IcyCombatControls() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, IcyCombatControlsConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BetterHorsesStatCapsConfig.SPEC);
        MinecraftForge.EVENT_BUS.addListener(HorseInteractionEvents::onMount);
        MinecraftForge.EVENT_BUS.addListener(BetterHorsesStatCaps::addHorseFeature);

        if (ModList.get().isLoaded("supplementaries")) {
            MinecraftForge.EVENT_BUS.addListener(SupplementariesCompat::addHorseFeature);
        }
    }
}
