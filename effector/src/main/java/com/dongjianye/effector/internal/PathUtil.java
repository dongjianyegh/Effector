package com.dongjianye.effector.internal;


import android.view.animation.PathInterpolator;


/**
 * @author dongjianye on 12/31/20
 */
public class PathUtil {

    private static final PathInterpolator sPathInterpolator = new PathInterpolator(0.17f, 0.17f, 0.2f, 1.0f);

    public static PathInterpolator getPathInterpolator() {
        return sPathInterpolator;
    }
}