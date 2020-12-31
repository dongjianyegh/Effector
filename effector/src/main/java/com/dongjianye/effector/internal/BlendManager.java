package com.dongjianye.effector.internal;

import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import androidx.annotation.MainThread;

/**
 * @author dongjianye on 12/31/20
 */
public class BlendManager {

    private static final HashMap<Integer, ArrayList<Blender>> sBlenders = new HashMap<>();

    @MainThread
    private BlendManager() {
    }

    @MainThread
    public static void onBlending(View view, Canvas canvas) {
        ArrayList<Blender> arrayList = sBlenders.get(view.hashCode());
        if (arrayList != null) {
            Iterator<Blender> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().blend(canvas);
            }
        }
    }

    @MainThread
    public static void removeBlender(View view, Blender aVar) {
        ArrayList<Blender> arrayList = sBlenders.get(view.hashCode());
        if (arrayList != null) {
            arrayList.remove(aVar);
            if (arrayList.isEmpty()) {
                sBlenders.remove(view.hashCode());
            }
        }
    }

    @MainThread
    public static void addBlender(View view, Blender blender) {
        if (sBlenders.get(view.hashCode()) == null) {
            sBlenders.put(view.hashCode(), new ArrayList<Blender>());
        }

        ArrayList<Blender> arrayList = sBlenders.get(view.hashCode());
        if (arrayList != null) {
            arrayList.add(blender);
        }
    }
}