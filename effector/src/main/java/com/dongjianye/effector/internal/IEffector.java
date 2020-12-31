package com.dongjianye.effector.internal;

import androidx.annotation.MainThread;

/**
 * @author dongjianye on 12/31/20
 */
public abstract class IEffector {

    /**
     * 按下时候的效果
     * @param touchX
     * @param touchY
     */
    @MainThread
    public abstract void onDown(final float touchX, final float touchY);

    /**
     * 抬起时候的效果
     * @param touchX
     * @param touchY
     */
    @MainThread
    public void b(final float touchX, final float touchY) {
    }
}