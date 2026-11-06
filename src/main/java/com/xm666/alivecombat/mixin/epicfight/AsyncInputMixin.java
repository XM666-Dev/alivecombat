package com.xm666.alivecombat.mixin.epicfight;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.xm666.alivecombat.handler.AsyncInputHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.client.events.engine.ControlEngine;

@OnlyIn(Dist.CLIENT)
public class AsyncInputMixin {
    @Mixin(ControlEngine.class)
    private static class ControlEngineMixin {
        @WrapMethod(method = "handleEpicFightKeyMappings", remap = false)
        private void wrap(Operation<Void> original) {
            if (!AsyncInputHandler.inputSynced) return;

            original.call();
        }
    }
}
