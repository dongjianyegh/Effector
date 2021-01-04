package com.dongjianye.effector.internal;

import android.content.res.Resources;

/**
 * @author dongjianye on 1/4/21
 */
public class DpUtils {


    public static int dp2pixel(int dp) {
        Resources system = Resources.getSystem();
        return (int) (system.getDisplayMetrics().density * dp + 0.5f);
    }
}