package compat.icycombatcontrols;

import net.minecraftforge.common.ForgeConfigSpec;

public final class IcyCombatControlsConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue HORSE_ENCUMBRANCE;
    public static final ForgeConfigSpec.DoubleValue HORSE_ENCUMBRANCE_MULTIPLIER;
    public static final ForgeConfigSpec.BooleanValue INCLUDE_CART_STORAGE;
    public static final ForgeConfigSpec.BooleanValue ONLY_WHILE_RIDDEN;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("supplementaries");

        HORSE_ENCUMBRANCE = builder
                .comment("Apply Supplementaries' Overencumbered effect to horses carrying too many filled sacks.")
                .define("horse_encumbrance", true);
        HORSE_ENCUMBRANCE_MULTIPLIER = builder
                .comment("Scales the sack weight carried by horses. 0.5 gives twice the capacity; 2.0 gives half.")
                .defineInRange("horse_encumbrance_multiplier", 1.0D, 0.0D, 10.0D);
        INCLUDE_CART_STORAGE = builder
                .comment("Include sacks in an attached Better Horses storage cart.")
                .define("include_cart_storage", true);
        ONLY_WHILE_RIDDEN = builder
                .comment("Only apply the effect while a player is riding the horse.")
                .define("only_while_ridden", false);

        builder.pop();
        SPEC = builder.build();
    }

    private IcyCombatControlsConfig() {
    }
}
