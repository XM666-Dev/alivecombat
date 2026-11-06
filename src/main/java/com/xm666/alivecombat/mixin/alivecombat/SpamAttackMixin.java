package com.xm666.alivecombat.mixin.alivecombat;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.xm666.alivecombat.handler.SpamAttackHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class SpamAttackMixin {
    @Mixin(Minecraft.class)
    private static class MinecraftMixin {
        @Shadow
        @Nullable
        public HitResult hitResult;

        @Redirect(method = "startAttack", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/Minecraft;missTime:I", ordinal = 1))
        private void redirectMissTime(Minecraft instance, int value) {
        }

        @Redirect(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V"))
        private void redirectResetAttackStrengthTicker(LocalPlayer instance) {
        }

        @WrapOperation(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
        private void wrapSwing(LocalPlayer instance, InteractionHand hand, Operation<Void> original) {
            SpamAttackHandler.sendSwingPacket = hitResult.getType() == HitResult.Type.ENTITY;
            original.call(instance, hand);
            SpamAttackHandler.sendSwingPacket = false;
        }
    }

    @Mixin(LocalPlayer.class)
    private static class LocalPlayerMixin {
        @WrapWithCondition(method = "swing", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"))
        private boolean wrapSend(ClientPacketListener instance, Packet<?> packet) {
            return SpamAttackHandler.sendSwingPacket;
        }
    }

    @Mixin(MultiPlayerGameMode.class)
    private static class MultiPlayerGameModeMixin {
        @Redirect(method = "stopDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V"))
        private void redirectResetAttackStrengthTicker(LocalPlayer instance) {
        }
    }
}
