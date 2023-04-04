package ca.skynetcloud.cobblemonbroadcast;

import ca.skynetcloud.cobblemonbroadcast.util.CobbleConfig;
import ca.skynetcloud.cobblemonbroadcast.util.DiscordWebhook;
import ca.skynetcloud.cobblemonbroadcast.util.Text;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.util.LocalizationUtilsKt;
import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.PlayerEvent;
import kotlin.Unit;
import net.minecraft.ChatFormatting;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static ca.skynetcloud.cobblemonbroadcast.util.CobbleConfig.endCode;

@Mod(Cobblemonbroadcast.MODID)
public class Cobblemonbroadcast {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobblemonbroadcast";

    public static final String MODNAME = "Cobblemon-Broadcaster";

    public static final String MODVERSION = "1.6.19045";
    
    public static final String DEVNAME = "SkyNetCloud";

    public static final Logger LOGGER = LogUtils.getLogger();


    public Cobblemonbroadcast() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CobbleConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CobbleConfig.SPEC);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        CaputerStuff();
        LOGGER.info(ChatFormatting.RED + MODNAME + " " + ChatFormatting.BLUE + MODVERSION + " Loaded" );
        LOGGER.info(ChatFormatting.GREEN + "Mod By:" + DEVNAME);

    }
    
    private void CaputerStuff() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat() / 2f;
        float b = rand.nextFloat() / 2f;
        Color randColor = new Color(r,g,b);



        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/" + endCode.get());
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, e -> {
            if (e.getPokemon().isLegendary() && CobbleConfig.isLegendaryEnable.get() && !e.getPokemon().getShiny())  {
                MutableComponent comp1 = e.getPlayer().getName().plainCopy();
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy();
                Component legend = Component.literal( ChatFormatting.BLUE + comp1.getString() + ChatFormatting.WHITE + CobbleConfig.LegendaryMessage.get() + ChatFormatting.GREEN + comp2.getString());
                if (CobbleConfig.discordHook.get()){
                    webhook.setAvatarUrl(CobbleConfig.setAvatarUrl.get());
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setColor(randColor)
                            .setAuthor("Pokemon Captured: " + e.getPokemon().getSpecies().getName(),"","https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+ e.getPokemon().getCaughtBall().getName().getPath() +".png")
                            .addField("Player Name: " + e.getPlayer().getName().getString(), "", false)
                            .addField("Nature: " + Text.capitalizeWord(e.getPokemon().getNature().getName().getPath().replace("_", " ")), "", false)
                            .addField("Ability: " + LocalizationUtilsKt.lang(Text.capitalize(e.getPokemon().getAbility().getName())).getString().replace("cobblemon.", " "), "", false)
                            .addField("Ball Used: " + Text.capitalizeWord(e.getPokemon().getCaughtBall().getName().getPath().replace("_", " ")), "", false)

                            .addField("Ivs: " + "(" + String.format("%.2f", e.getPokemon().getIvs().getOrDefault(Stats.HP) + e.getPokemon().getIvs().getOrDefault(Stats.ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPEED)  * 100.0D / 510.0D).toString() + ")", "", false)
                            .addField("Hp", e.getPokemon().getIvs().get(Stats.HP).toString(), true)
                            .addField("Atk", e.getPokemon().getIvs().get(Stats.ATTACK).toString(), true)
                            .addField("Def", e.getPokemon().getIvs().get(Stats.DEFENCE).toString(), true)

                            .addField("SpA", e.getPokemon().getIvs().get(Stats.SPECIAL_ATTACK).toString(), true)
                            .addField("SpD", e.getPokemon().getIvs().get(Stats.SPECIAL_DEFENCE).toString(), true)
                            .addField("Spe", e.getPokemon().getIvs().get(Stats.SPEED).toString(), true)

                            .setThumbnail("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + e.getPokemon().getSpecies().getNationalPokedexNumber() + ".png"));
                    try {
                        webhook.execute();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
                    player.sendSystemMessage(legend);
                }
            }

            if (e.getPokemon().isUltraBeast() &&  CobbleConfig.isUltraBeastEnable.get() && !e.getPokemon().getShiny()) {
                MutableComponent comp1 = e.getPlayer().getName().plainCopy();
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy();
                Component ub = Component.literal( ChatFormatting.BLUE + comp1.getString() + ChatFormatting.WHITE + CobbleConfig.UltraBeastMessage.get() + ChatFormatting.YELLOW + comp2.getString());

                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    player.sendSystemMessage(ub);
                }

                if (CobbleConfig.discordHook.get()) {
                    webhook.setAvatarUrl(CobbleConfig.setAvatarUrl.get());
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setColor(randColor)
                            .setAuthor("Pokemon Captured: " + e.getPokemon().getSpecies().getName(),"","https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+ e.getPokemon().getCaughtBall().getName().getPath() +".png")
                            .addField("Player Name: " + e.getPlayer().getName().getString(), "", false)
                            .addField("Nature: " + Text.capitalizeWord(e.getPokemon().getNature().getName().getPath().replace("_", " ")), "", false)
                            .addField("Ability: " + LocalizationUtilsKt.lang(Text.capitalize(e.getPokemon().getAbility().getName())).getString().replace("cobblemon.", " "), "", false)
                            .addField("Ball Used: " + Text.capitalizeWord(e.getPokemon().getCaughtBall().getName().getPath().replace("_", " ")), "", false)

                            .addField("Ivs: " + "(" + String.format("%.2f", e.getPokemon().getIvs().getOrDefault(Stats.HP) + e.getPokemon().getIvs().getOrDefault(Stats.ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPEED)  * 100.0D / 510.0D).toString() + ")", "", false)
                            .addField("Hp", e.getPokemon().getIvs().get(Stats.HP).toString(), true)
                            .addField("Atk", e.getPokemon().getIvs().get(Stats.ATTACK).toString(), true)
                            .addField("Def", e.getPokemon().getIvs().get(Stats.DEFENCE).toString(), true)

                            .addField("SpA", e.getPokemon().getIvs().get(Stats.SPECIAL_ATTACK).toString(), true)
                            .addField("SpD", e.getPokemon().getIvs().get(Stats.SPECIAL_DEFENCE).toString(), true)
                            .addField("Spe", e.getPokemon().getIvs().get(Stats.SPEED).toString(), true)

                            .setThumbnail("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + e.getPokemon().getSpecies().getNationalPokedexNumber() + ".png"));
                    try {
                        webhook.execute();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            if (e.getPokemon().getShiny() && CobbleConfig.isShinyEnable.get()) {
                MutableComponent comp1 = e.getPlayer().getName().plainCopy();
                MutableComponent comp2 = e.getPokemon().getDisplayName();
                Component shiny = Component.literal( ChatFormatting.BLUE + comp1.getString() + ChatFormatting.WHITE + CobbleConfig.ShinyMessage.get() + ChatFormatting.GOLD + comp2.getString());


                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
                    player.sendSystemMessage(shiny);
                }

                if (CobbleConfig.discordHook.get()) {
                    webhook.setAvatarUrl(CobbleConfig.setAvatarUrl.get());
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setColor(randColor)
                            .setAuthor("Pokemon Captured: " + e.getPokemon().getSpecies().getName(),"","https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+ e.getPokemon().getCaughtBall().getName().getPath() +".png")
                            .addField("Player Name: " + e.getPlayer().getName().getString(), "", false)
                            .addField("Nature: " + Text.capitalizeWord(e.getPokemon().getNature().getName().getPath().replace("_", " ")), "", false)
                            .addField("Ability: " + LocalizationUtilsKt.lang(Text.capitalize(e.getPokemon().getAbility().getName())).getString().replace("cobblemon.", " "), "", false)
                            .addField("Ball Used: " + Text.capitalizeWord(e.getPokemon().getCaughtBall().getName().getPath().replace("_", " ")), "", false)

                            .addField("Ivs:", "("+ String.format("%.2f", e.getPokemon().getIvs().getOrDefault(Stats.HP) + e.getPokemon().getIvs().getOrDefault(Stats.ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPEED)  * 100.0D / 510.0D).toString() + ")", false)
                            .addField("Hp", e.getPokemon().getIvs().get(Stats.HP).toString(), true)
                            .addField("Atk", e.getPokemon().getIvs().get(Stats.ATTACK).toString(), true)
                            .addField("Def", e.getPokemon().getIvs().get(Stats.DEFENCE).toString(), true)

                            .addField("SpA", e.getPokemon().getIvs().get(Stats.SPECIAL_ATTACK).toString(), true)
                            .addField("SpD", e.getPokemon().getIvs().get(Stats.SPECIAL_DEFENCE).toString(), true)
                            .addField("Spe", e.getPokemon().getIvs().get(Stats.SPEED).toString(), true)

                            .setThumbnail("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/" + e.getPokemon().getSpecies().getNationalPokedexNumber() + ".png"));
                    try {
                        webhook.execute();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            if (!e.getPokemon().isUltraBeast() && !e.getPokemon().getShiny() && !e.getPokemon().isLegendary() && CobbleConfig.isNormalEnable.get()) {

                MutableComponent comp1 = e.getPlayer().getName().plainCopy();
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy();
                Component normal = Component.literal( ChatFormatting.BLUE + comp1.getString() + ChatFormatting.WHITE + CobbleConfig.NormalLMessage.get() + ChatFormatting.DARK_AQUA + comp2.getString());

                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
                    player.sendSystemMessage(normal);
                }

                if (CobbleConfig.discordHook.get()) {
                    webhook.setAvatarUrl(CobbleConfig.setAvatarUrl.get());
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setColor(randColor)
                            .setAuthor("Pokemon Captured: " + e.getPokemon().getSpecies().getName(),"","https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+ e.getPokemon().getCaughtBall().getName().getPath() +".png")
                            .addField("Player Name: " + e.getPlayer().getName().getString(), "", false)
                            .addField("Nature: " + Text.capitalizeWord(e.getPokemon().getNature().getName().getPath().replace("_", " ")), "", false)
                            .addField("Ability: " + LocalizationUtilsKt.lang(Text.capitalize(e.getPokemon().getAbility().getName())).getString().replace("cobblemon.", " "), "", false)
                            .addField("Ball Used: " + Text.capitalizeWord(e.getPokemon().getCaughtBall().getName().getPath().replace("_", " ")), "", false)

                            .addField("Ivs:", "("+ String.format("%.2f", e.getPokemon().getIvs().getOrDefault(Stats.HP) + e.getPokemon().getIvs().getOrDefault(Stats.ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_ATTACK) + e.getPokemon().getIvs().getOrDefault(Stats.SPECIAL_DEFENCE) + e.getPokemon().getIvs().getOrDefault(Stats.SPEED)  * 100.0D / 510.0D).toString() + ")", false)
                            .addField("Hp", e.getPokemon().getIvs().get(Stats.HP).toString(), true)
                            .addField("Atk", e.getPokemon().getIvs().get(Stats.ATTACK).toString(), true)
                            .addField("Def", e.getPokemon().getIvs().get(Stats.DEFENCE).toString(), true)

                            .addField("SpA", e.getPokemon().getIvs().get(Stats.SPECIAL_ATTACK).toString(), true)
                            .addField("SpD", e.getPokemon().getIvs().get(Stats.SPECIAL_DEFENCE).toString(), true)
                            .addField("Spe", e.getPokemon().getIvs().get(Stats.SPEED).toString(), true)

                            .setThumbnail("https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+e.getPokemon().getCaughtBall().getName().toString().trim()+".png"));
                    try {
                        webhook.execute();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            return Unit.INSTANCE;
        });
    }



}
