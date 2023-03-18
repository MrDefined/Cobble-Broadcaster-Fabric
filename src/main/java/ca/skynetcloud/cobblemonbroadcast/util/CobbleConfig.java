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

    public static ConfigValue<String> webhookEmbedTitle;

    public static ConfigValue<String> LegendaryMessage;
    public static ConfigValue<String> UltraBeastMessage;
    public static ConfigValue<String> ShinyMessage;
    public static ConfigValue<String> NormalLMessage;
    static {

        builder.push("Broadcast Settings");
        isLegendaryEnable = builder.comment("enabling or disabling Broadcast Message for Legendary Pokemon").translation("islegendaryenabled").define("islegendaryenabled", true);
        isUltraBeastEnable = builder.comment("enabling or disabling Broadcast Message for Ultra Beast Pokemon").translation("isubenabled").define("isubenabled", true);
        isShinyEnable = builder.comment("enabling or disabling Broadcast Message for Shiny Pokemon").translation("isshinyenabled").define("isshinyenabled", true);
        isNormalEnable = builder.comment("enabling or disabling Broadcast Message for Normal Pokemon").translation("isnormalenabled").define("isnormalenabled", false);

        LegendaryMessage = builder.comment("Captured Message for Legendary Pokemon").translation("legendmessage").define("legendmessagepath", " has captured Legendary ");
        UltraBeastMessage = builder.comment("Captured Message for UltraBeast Pokemon").translation("ubmessage").define("ubmessagepath", " has captured Ultra Beast ");
        ShinyMessage = builder.comment("Captured Message for Shiny Pokemon").translation("shinymessage").define("shinymessagepath", " has captured Shiny ");
        NormalLMessage = builder.comment("Captured Message for Normal Pokemon").translation("normalmessage").define("normalmessagepath", " has captured Normal ");
        builder.pop();

        builder.push("Discord Webhook Settings");
        discordHook = builder.comment("Enable or disable DiscordWebhook support").translation("discordhook").define("discordhook", false);
        endCode = builder.comment("Webhook End code of url").translation("endcode").define("endcode", "End Part of Webhook URL");
        webhookName = builder.comment("Webhook name").translation("webhookname").define("webhookname", "Cobble-Broadcaster");

        webhookEmbedTitle = builder.comment("Webhook Embed Title").translation("webhookembedtitle").define("webhookembedtitle", "Pokemon Captured by: ");
        builder.pop();
        SPEC = builder.build();



    }
}

