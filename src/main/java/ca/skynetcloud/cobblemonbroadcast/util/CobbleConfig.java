package ca.skynetcloud.cobblemonbroadcast.util;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class CobbleConfig {
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static BooleanValue isNormalEnable;
    public static BooleanValue isLegendaryEnable;
    public static BooleanValue isUltraBeastEnable;
    public static BooleanValue isShinyEnable;

    public static BooleanValue discordHook;

    public static ConfigValue<String> endCode;
    public static ConfigValue<String> webhookName;


    static {

        builder.push("Broadcast Settings");
        isLegendaryEnable = builder.comment("enabling or disabling Broadcast Message for Legendary Pokemon").translation("islegendaryenabled").define("islegendaryenabled", true);
        isUltraBeastEnable = builder.comment("enabling or disabling Broadcast Message for Ultra Beast Pokemon").translation("isubenabled").define("isubenabled", true);
        isShinyEnable = builder.comment("enabling or disabling Broadcast Message for Shiny Pokemon").translation("isshinyenabled").define("isshinyenabled", true);
        isNormalEnable = builder.comment("enabling or disabling Broadcast Message for Normal Pokemon").translation("isnormalenabled").define("isnormalenabled", false);
        builder.pop();

        builder.push("Discord Webhook Settings");
        discordHook = builder.comment("Enable or disable DiscordWebhook support").translation("discordhook").define("discordhook", false);
        endCode = builder.comment("Webhook End code of url").translation("endcode").define("endcode", "10780232141243872095343/ZpFBl-lGgZziiMg5JP7vLXXxuBPAteyq1OkLLGS8qANJyA1vE7UIZxS9bD16mb1UCGKi");
        webhookName = builder.comment("Webhook name").translation("webhookname").define("webhookname", "Cobble-Broadcaster");
        builder.pop();
        SPEC = builder.build();

    }
}

