package com.dongjianye.effector.internal;

import java.util.Random;

/**
 * @author dongjianye on 12/31/20
 */
public class RandomUtil {

    private final static Random sRandom = new Random();

    private RandomUtil() {
    }

    public static int random(int start, int end) {
        return start + sRandom.nextInt(end - start + 1);
    }

    public static int nextInt(int value) {
        return sRandom.nextInt(value);
    }
}