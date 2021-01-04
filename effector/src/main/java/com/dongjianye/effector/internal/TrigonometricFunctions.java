package com.dongjianye.effector.internal;

/**
 * @author dongjianye on 1/4/21
 */
public final class TrigonometricFunctions {

    public static float cos(int degree, float radius) {
        return ((float) Math.cos(Math.toRadians((double) degree))) * radius;
    }

    public static float sin(int degree, float radius) {
        return ((float) Math.sin(Math.toRadians((double) degree))) * radius;
    }
}