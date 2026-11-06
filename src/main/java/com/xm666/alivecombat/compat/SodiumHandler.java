package com.xm666.alivecombat.compat;

import org.spongepowered.asm.mixin.Mixins;

public class SodiumHandler {
    public static float getParticleRollDirection() {
        return 1.0F;
    }

    private static boolean isSodiumLoaded() {
        return isMixinApplied("net.minecraft.client.particle.SingleQuadParticle", "net.caffeinemc.mods.sodium.mixin.features.render.particle.SingleQuadParticleMixin");
    }

    public static boolean isMixinApplied(String targetClass, String mixinClass) {
        return Mixins.getMixinsForClass(targetClass).stream()
                .anyMatch(mixin -> mixin.getClassName().equals(mixinClass));
    }
}
