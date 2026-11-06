package com.xm666.alivecombat.handler;

import com.xm666.alivecombat.MixinConfig;
import com.xm666.alivecombat.compat.ShoulderSurfingHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class AsyncInputHandler {
    public static boolean inputSynced = true;

    public static class AsyncInputClient {
        @SubscribeEvent
        public static void onRenderFramePost(TickEvent.RenderTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;

            var mc = Minecraft.getInstance();
            if (mc.player == null) return;

            if (ModList.get().isLoaded("shouldersurfing")) {
                ShoulderSurfingHandler.tick();
            }

            inputSynced = false;
            mc.handleKeybinds();
            inputSynced = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class AsyncInputConfig {
        public static void init(IEventBus modEventBus) {
            modEventBus.register(AsyncInputConfig.class);
        }

        @SubscribeEvent
        public static void onModConfigLoading(ModConfigEvent.Loading event) {
            if (!MixinConfig.ASYNC_INPUT_ENABLED.get()) return;

            MinecraftForge.EVENT_BUS.register(AsyncInputClient.class);
        }
    }
}
