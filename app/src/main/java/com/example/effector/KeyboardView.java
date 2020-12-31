package com.example.effector;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.dongjianye.effector.internal.BlendManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author dongjianye on 12/31/20
 */
public class KeyboardView extends FrameLayout {

    public KeyboardView(@NonNull Context context) {
        super(context);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BlendManager.onBlending(this, canvas);
    }
}