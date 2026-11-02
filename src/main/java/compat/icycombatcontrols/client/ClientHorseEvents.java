package compat.icycombatcontrols.client;

import compat.icycombatcontrols.IcyCombatControls;
import icy.betterhorses.net.BhHorseKind;
import icy.betterhorses.net.IcysBetterHorsesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IcyCombatControls.MOD_ID, value = Dist.CLIENT)
public final class ClientHorseEvents {
    private ClientHorseEvents() {
    }

    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (event.isMounting()
                && event.getEntityMounting() == minecraft.player
                && event.getEntityBeingMounted() instanceof AbstractHorse horse
                && BhHorseKind.managed(horse)) {
            minecraft.execute(IcysBetterHorsesClient::sendChargeChoice);
        }
    }
}
