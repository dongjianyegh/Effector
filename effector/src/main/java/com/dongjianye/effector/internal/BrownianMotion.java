package com.dongjianye.effector.internal;

import androidx.annotation.MainThread;

/**
 * @author dongjianye on 12/31/20
 */
public class BrownianMotion {

    private float f3542a = 0.25f;

    private float f3543b = 0.3f;

    private int positionFractalLevel = 3;

    private final float f3545d = 1.3333334f;

    private final float[] mPositionSequence = new float[6];

    private final Vector3 mPosition = new Vector3(0.0f, 0.0f, 0.0f);
    private final FrameRateCounter mFrameRate = new FrameRateCounter();

    private final Vector3 mScale;

    @MainThread
    public BrownianMotion(Vector3 scale) {
        mScale = scale;
        rehash();
        mFrameRate.countFrameRate();
    }

    @MainThread
    private void rehash() {
        for (int i = 0; i <= 5; i++) {
            mPositionSequence[i] = RandomUtil.random(-10000, 0);
        }
    }

    @MainThread
    public final void a(float f2) {
        f3542a = f2;
    }

    @MainThread
    public final void update() {

        final float rate = mFrameRate.countFrameRate();
        for (int i = 0; i <= 2; i++) {
            mPositionSequence[i] = mPositionSequence[i] + (f3542a * rate);
        }
        Vector3 tempPosition = new Vector3(
                PerlinNoise.overlap(this.mPositionSequence[0], this.positionFractalLevel),
                PerlinNoise.overlap(this.mPositionSequence[1], this.positionFractalLevel),
                PerlinNoise.overlap(this.mPositionSequence[2], this.positionFractalLevel));

        tempPosition.scale(mScale);

        tempPosition.setX(tempPosition.getX() * this.f3543b * this.f3545d);
        tempPosition.setY(tempPosition.getY() * this.f3543b * this.f3545d);
        tempPosition.setZ(tempPosition.getZ() * this.f3543b * this.f3545d);

        mPosition.setX(tempPosition.getX());
        mPosition.setY(tempPosition.getY());
        mPosition.setZ(tempPosition.getZ());
    }

    @MainThread
    public final Vector3 getPosition() {
        return this.mPosition;
    }
}