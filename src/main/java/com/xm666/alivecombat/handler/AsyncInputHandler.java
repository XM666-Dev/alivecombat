package com.xm666.alivecombat.handler;

import com.xm666.alivecombat.AliveCombat;
import com.xm666.alivecombat.MixinConfig;
import com.xm666.alivecombat.compat.ShoulderSurfingHandler;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;

public class AsyncInputHandler {
    public static boolean inputSynced = true;

    public static class AsyncInputClient {
        @SubscribeEvent
        public static void onRenderFramePost(RenderFrameEvent.Post event) {
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

    @EventBusSubscriber(modid = AliveCombat.MODID, value = Dist.CLIENT)
    public static class AsyncInputConfig {
        @SubscribeEvent
        public static void onModConfigLoading(ModConfigEvent.Loading event) {
            if (!MixinConfig.ASYNC_INPUT_ENABLED.get()) return;

            NeoForge.EVENT_BUS.register(AsyncInputClient.class);
        }
    }
}
