package com.xm666.alivecombat.compat;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.util.Collections;
import java.util.Set;

public class SodiumHandler {
    public static float getParticleRollDirection() {
        return 1.0F;
    }

    private static boolean isSodiumLoaded() {
        return isMixinApplied("net.minecraft.client.particle.SingleQuadParticle", "net.caffeinemc.mods.sodium.mixin.features.render.particle.SingleQuadParticleMixin");
    }

    private static boolean isMixinApplied(String targetClass, String mixinClass) {
        return getMixinsForClass(targetClass).stream()
                .anyMatch(mixin -> mixin.getClassName().equals(mixinClass));
    }

    private static Set<IMixinInfo> getMixinsForClass(String className) {
        ClassInfo classInfo = ClassInfo.fromCache(className);
        if (classInfo != null) {
            try {
                var field = ClassInfo.class.getDeclaredField("mixins");
                field.setAccessible(true);
                return (Set<IMixinInfo>) field.get(classInfo);
            } catch (Exception ignored) {
            }
        }
        return Collections.emptySet();
    }
}
