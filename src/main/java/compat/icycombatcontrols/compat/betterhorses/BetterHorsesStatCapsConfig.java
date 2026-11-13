package compat.icycombatcontrols.compat.betterhorses;

import icy.betterhorses.net.IcysBetterHorses;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BetterHorsesStatCapsConfig {
    public static final ForgeConfigSpec SPEC;
    private static final Limits OTHER_HORSES;
    private static final Map<String, Limits> BREEDS;

    public record Range(ForgeConfigSpec.DoubleValue minimum, ForgeConfigSpec.DoubleValue maximum) {
        public double min() {
            return Math.min(minimum.get(), maximum.get());
        }

        public double max() {
            return Math.max(minimum.get(), maximum.get());
        }
    }

    public record Limits(Range health, Range speed, Range jump) {}

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Natural stat ranges for new Better Horses. The maximum also caps the effective stat after bond and other bonuses.")
                .push("better_horses_stats");
        OTHER_HORSES = define(builder, "other_horses", 15, 30, 0.1125, 0.3375, 0.40, 1.00);
        builder.push("breeds");
        Map<String, Limits> breeds = new LinkedHashMap<>();
        add(builder, breeds, new String[]{"thoroughbred", "arabian", "quarter"},
                15, 20, 0.2546, 0.3472, 0.72, 1.02);
        add(builder, breeds, new String[]{"friesian", "andalusian", "mustang"},
                25, 40, 0.2083, 0.3472, 0.57, 0.97);
        add(builder, breeds, new String[]{"morgan", "american_paint", "appaloosa"},
                15, 30, 0.1852, 0.3472, 0.57, 0.84);
        add(builder, breeds, new String[]{"percheron", "clydesdale", "shire", "belgian"},
                35, 50, 0.1620, 0.2315, 0.38, 0.72);
        add(builder, breeds, new String[]{"icelandic", "haflinger"},
                25, 40, 0.1852, 0.3009, 0.57, 0.84);
        builder.pop(2);
        BREEDS = Map.copyOf(breeds);
        SPEC = builder.build();
    }

    public static Limits forBreed(ResourceLocation id) {
        return id != null && id.getNamespace().equals(IcysBetterHorses.MOD_ID)
                ? BREEDS.getOrDefault(id.getPath(), OTHER_HORSES)
                : OTHER_HORSES;
    }

    private static void add(ForgeConfigSpec.Builder builder, Map<String, Limits> breeds, String[] names,
                            double healthMin, double healthMax, double speedMin, double speedMax,
                            double jumpMin, double jumpMax) {
        for (String name : names) {
            breeds.put(name, define(builder, name, healthMin, healthMax, speedMin, speedMax, jumpMin, jumpMax));
        }
    }

    private static Limits define(ForgeConfigSpec.Builder builder, String name,
                                 double healthMin, double healthMax, double speedMin, double speedMax,
                                 double jumpMin, double jumpMax) {
        builder.push(name);
        Limits limits = new Limits(
                range(builder, "health", healthMin, healthMax),
                range(builder, "speed", speedMin, speedMax),
                range(builder, "jump_strength", jumpMin, jumpMax));
        builder.pop();
        return limits;
    }

    private static Range range(ForgeConfigSpec.Builder builder, String name, double min, double max) {
        double lowerBound = name.equals("health") ? 1.0D : 0.0001D;
        return new Range(
                builder.comment("Minimum natural " + name + " for newly spawned horses. If greater than maximum, the values are swapped.")
                        .defineInRange(name + "_min", min, lowerBound, 1024.0D),
                builder.comment("Maximum natural and effective " + name + ", including bond bonuses.")
                        .defineInRange(name + "_max", max, lowerBound, 1024.0D));
    }

    private BetterHorsesStatCapsConfig() {}
}
