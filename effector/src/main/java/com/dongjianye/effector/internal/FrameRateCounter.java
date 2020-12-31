package com.dongjianye.effector.internal;

import android.os.SystemClock;

/**
 * @author dongjianye on 12/31/20
 */
public class FrameRateCounter {

    private long mLastTime;

    public final float countFrameRate() {
        final long uptimeMillis = SystemClock.uptimeMillis();
        long interval = uptimeMillis - mLastTime;
        float f2 = mLastTime > 0.0f ? interval / 1000.0f : 0.0f;
        this.mLastTime = uptimeMillis;
        return Math.min(0.021f, f2);
    }
}