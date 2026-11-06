package com.xm666.alivecombat.handler;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public enum AttackParticleTransform {
    GENERAL {
        @Override
        public float getRoll() {
            return 0.0F;
        }

        @Override
        public Vec3 getOffset() {
            return new Vec3(0.0, -0.25, 0.0);
        }
    },
    CRITICAL {
        @Override
        public float getRoll() {
            return -Mth.HALF_PI;
        }

        @Override
        public Vec3 getOffset() {
            return new Vec3(0.25, -0.5, 0.0);
        }
    },
    SPRINT {
        @Override
        public float getRoll() {
            return -Mth.HALF_PI * 0.5F;
        }

        @Override
        public Vec3 getOffset() {
            return new Vec3(0.125, -0.125, 0.0);
        }
    };

    public abstract float getRoll();

    public abstract Vec3 getOffset();

    public Vec3 getPosition(Vec3 hitPoint, Vec3 viewVector) {
        var quaternionf = new Quaternionf().lookAlong(viewVector.toVector3f(), new Vector3f(0.0F, -1.0F, 0.0F));
        var offset = new Vec3(quaternionf.transform(getOffset().reverse().toVector3f())).add(viewVector.scale(-0.5));
        return hitPoint.add(offset);
    }
}
