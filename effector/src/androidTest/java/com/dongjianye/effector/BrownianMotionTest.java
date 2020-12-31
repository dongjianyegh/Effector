package com.dongjianye.effector;

import android.content.Context;
import android.util.Log;

import com.dongjianye.effector.internal.BrownianMotion;
import com.dongjianye.effector.internal.Vector3;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

/**
 * @author dongjianye on 12/31/20
 */
@RunWith(AndroidJUnit4.class)
public class BrownianMotionTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.dongjianye.effector.test", appContext.getPackageName());
    }

    @Test
    public void testBrownian() {
        BrownianMotion noise = new BrownianMotion(new Vector3(100, 100, 1000));

        noise.update();
        Vector3 position = noise.getPosition();

        Log.d("BrownianMotionTest", position.toString());

        noise = new BrownianMotion(new Vector3(100, 100, 1000));

        noise.update();
        position = noise.getPosition();

        Log.d("BrownianMotionTest", position.toString());

        noise = new BrownianMotion(new Vector3(100, 100, 1000));

        noise.update();
        position = noise.getPosition();

        Log.d("BrownianMotionTest", position.toString());
    }
}