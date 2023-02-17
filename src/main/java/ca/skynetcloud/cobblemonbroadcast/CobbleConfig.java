package ca.skynetcloud.cobblemonbroadcast;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CobbleConfig {
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final Config CONFIG;

    static {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    public static class Config {

        public static BooleanValue isNormalEnable;
        public static BooleanValue isLegendaryEnable;
        public static BooleanValue isUltraBeastEnable;

        public static BooleanValue isShinyEnable;


        public Config(ForgeConfigSpec.Builder builder) {

            builder.push("Broadcast Settins");
            isLegendaryEnable = builder.comment("for enable or disable the for Legendary Broadcast").translation("islegendaryenabled").define("islegendaryenabled", true);
            isUltraBeastEnable = builder.comment("for enable or disable the for Ultra Beast Broadcast").translation("isubenabled").define("isubenabled", true);
            isShinyEnable = builder.comment("for enable or disable the for Shiny Pokemon Broadcast").translation("isshinyenabled").define("isshinyenabled", true);
            isNormalEnable = builder.comment("for enable or disable of the Normal Pokemon Broadcast").translation("isnormalenabled").define("isnormalenabled", false);

        }

    }
}
