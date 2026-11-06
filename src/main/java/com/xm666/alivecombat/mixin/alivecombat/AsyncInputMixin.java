package com.xm666.alivecombat.mixin.alivecombat;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.xm666.alivecombat.handler.AsyncInputHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public class AsyncInputMixin {
    @Mixin(Minecraft.class)
    private static class MinecraftMixin {
        @WrapWithCondition(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V"))
        private boolean wrapContinueAttack(Minecraft instance, boolean direction) {
            return AsyncInputHandler.inputSynced;
        }
    }
}
