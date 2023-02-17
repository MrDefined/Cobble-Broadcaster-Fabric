package ca.skynetcloud.cobblemonbroadcast;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.logging.LogUtils;
import kotlin.Unit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;

import java.awt.*;
import java.util.Objects;

@Mod(Cobblemonbroadcast.MODID)
public class Cobblemonbroadcast {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobblemonbroadcast";

    public static final String MODNAME = "Cobblemon-Broadcaster";

    public static final String MODVERSION = "0.0.1";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Cobblemonbroadcast() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CobbleConfig.CONFIG_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CobbleConfig.CONFIG_SPEC);
        CobblemonEvents.INSTANCE.getPOKEMON_CAPTURED().subscribe(Priority.HIGH, e -> {

            if (e.getPokemon().isLegendary() && CobbleConfig.Config.isLegendaryEnable.get())  {

                MutableComponent comp1 = e.getPlayer().getName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
                Component legend = Component.translatable("cobblemonbroadcast.captured.legend", comp1, comp2);
                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
                    player.sendSystemMessage(legend);
                }

            }

            if (e.getPokemon().isUltraBeast() &&  CobbleConfig.Config.isUltraBeastEnable.get()) {
                MutableComponent comp1 = e.getPlayer().getName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
                Component ub = Component.translatable("cobblemonbroadcast.captured.ub", comp1, comp2);
                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
                    player.sendSystemMessage(ub);
                }

            }

            if (e.getPokemon().getShiny() && CobbleConfig.Config.isShinyEnable.get()) {
                MutableComponent comp1 = e.getPlayer().getName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
                Component shiny = Component.translatable("cobblemonbroadcast.captured.shiny", comp1, comp2);

                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
                    player.sendSystemMessage(shiny);
                }

            }

            if (!e.getPokemon().isUltraBeast() && !e.getPokemon().getShiny() && !e.getPokemon().isLegendary() && CobbleConfig.Config.isNormalEnable.get()) {

                MutableComponent comp1 = e.getPlayer().getName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
                MutableComponent comp2 = e.getPokemon().getDisplayName().plainCopy().setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
                Component normal = Component.translatable("cobblemonbroadcast.captured.normal", comp1, comp2);

                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()){
                    player.sendSystemMessage(normal);
                }

            }

            return Unit.INSTANCE;
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info(MODNAME + " " + MODVERSION + " Loaded" );
    }




}
