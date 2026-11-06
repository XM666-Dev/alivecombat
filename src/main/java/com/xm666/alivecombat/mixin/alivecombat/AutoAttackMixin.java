package com.xm666.alivecombat.mixin.alivecombat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.xm666.alivecombat.handler.AutoAttackHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class AutoAttackMixin {
    @Mixin(Minecraft.class)
    private static class MinecraftMixin {
        @Shadow
        @Nullable
        public HitResult hitResult;

        @Shadow
        @Nullable
        public LocalPlayer player;

        @ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal = 13))
        private boolean modifyConsumeClick(boolean clicked, @Share("autoAttacked") LocalBooleanRef autoAttacked) {
            if (clicked) {
                if (!(hitResult instanceof EntityHitResult)) {
                    AutoAttackHandler.timer.start();
                }
                return true;
            }

            if (!autoAttacked.get() && AutoAttackHandler.isAttackReady()) {
                autoAttacked.set(true);
                return true;
            }

            return false;
        }

        @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;startAttack()Z"))
        private boolean wrapStartAttack(Minecraft instance, Operation<Boolean> original, @Share("autoAttacked") LocalBooleanRef autoAttacked) {
            if (!autoAttacked.get() || AutoAttackHandler.canSwingHand(player))
                return original.call(instance);

            AutoAttackHandler.disableSwingHand = true;
            var result = original.call(instance);
            AutoAttackHandler.disableSwingHand = false;
            return result;
        }
    }

    @Mixin(MultiPlayerGameMode.class)
    private static class MultiPlayerGameModeMixin {
        @Inject(method = "destroyBlock", at = @At(value = "RETURN", ordinal = 5))
        private void onDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
            AutoAttackHandler.timer.stop();
        }
    }
}
