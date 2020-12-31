package com.dongjianye.effector;

import android.content.Context;
import android.util.Log;

import com.dongjianye.effector.internal.PerlinNoise;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PerlinNoiseTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.dongjianye.effector.test", appContext.getPackageName());
    }

    @Test
    public void testNoise() {
        PerlinNoise noise = new PerlinNoise();

        final float result = noise.overlap(0.2f, 3);

        Log.d("PerlinNoiseTest", "" + result);
    }
}