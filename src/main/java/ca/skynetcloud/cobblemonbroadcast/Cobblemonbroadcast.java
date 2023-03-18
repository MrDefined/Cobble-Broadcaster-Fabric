package ca.skynetcloud.cobblemonbroadcast;

import ca.skynetcloud.cobblemonbroadcast.util.CobbleConfig;
import ca.skynetcloud.cobblemonbroadcast.util.DiscordWebHookSystem;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.logging.LogUtils;
import kotlin.Unit;
import net.minecraft.ChatFormatting;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.loading.ServerModLoader;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;

import static ca.skynetcloud.cobblemonbroadcast.util.CobbleConfig.endCode;

@Mod(Cobblemonbroadcast.MODID)
public class Cobblemonbroadcast {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobblemonbroadcast";

    public static final String MODNAME = "Cobblemon-Broadcaster";

    public static final String MODVERSION = "1.4.19045";

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
    }

    private void CaputerStuff() {
        DiscordWebHookSystem  webhook = new DiscordWebHookSystem("https://discord.com/api/webhooks/" + endCode.get());
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, e -> {

            Pokemon poke = new Pokemon();

            if (e.getPokemon().isLegendary() && CobbleConfig.isLegendaryEnable.get() && !e.getPokemon().getShiny())  {

                MutableComponent comp1 = e.getPlayer().getName().plainCopy();
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy();
                Component legend = Component.literal( ChatFormatting.BLUE + comp1.getString() + ChatFormatting.WHITE + CobbleConfig.LegendaryMessage.get() + ChatFormatting.GREEN + comp2.getString());

                if (CobbleConfig.discordHook.get()){
                    webhook.setAvatarUrl("https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+ e.getPokemon().getCaughtBall().getName().getPath() +".png");
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebHookSystem.EmbedObject()
                            .setTitle(CobbleConfig.webhookEmbedTitle.get() + e.getPlayer().getName().getString())
                            .setColor(Color.WHITE)
                            .addField("Captured Pokemon: ", e.getPokemon().getDisplayName().getString(), true)
                            .addField("Ball Used: ", e.getPokemon().getCaughtBall().getName().getPath(), true)
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
                    webhook.setAvatarUrl("https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+ e.getPokemon().getCaughtBall().getName().getPath() +".png");
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebHookSystem.EmbedObject()
                            .setTitle(CobbleConfig.webhookEmbedTitle.get() + e.getPlayer().getName().getString())
                            .setColor(Color.WHITE)
                            .addField("Captured Pokemon: ", e.getPokemon().getDisplayName().getString(), true)
                            .addField("Ball Used: ", e.getPokemon().getCaughtBall().getName().getPath(), true)
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
                    webhook.setAvatarUrl("https://raw.githubusercontent.com/SkyNetCloud/pokesprite/master/items/ball/"+ e.getPokemon().getCaughtBall().getName().getPath() +".png");
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebHookSystem.EmbedObject()
                            .setTitle(CobbleConfig.webhookEmbedTitle.get() + e.getPlayer().getName().getString())
                            .setColor(Color.WHITE)
                            .addField("Captured Pokemon: ", e.getPokemon().getDisplayName().getString(), true)
                            .addField("Ball Used: ", e.getPokemon().getCaughtBall().getName().getPath(), true)
                            .setThumbnail("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + e.getPokemon().getSpecies().getNationalPokedexNumber() + ".png"));
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
                    webhook.setAvatarUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + e.getPokemon().getSpecies().getNationalPokedexNumber() + ".png");
                    webhook.setUsername(CobbleConfig.webhookName.get());
                    webhook.setTts(false);
                    webhook.addEmbed(new DiscordWebHookSystem.EmbedObject()
                            .setTitle(CobbleConfig.webhookEmbedTitle.get() + e.getPlayer().getName().getString())
                            .setColor(Color.WHITE)
                            .addField("Captured Pokemon", e.getPokemon().getDisplayName().getString(), true)
                            .addField("Ball Used:", e.getPokemon().getCaughtBall().getName().getPath(), true)
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
