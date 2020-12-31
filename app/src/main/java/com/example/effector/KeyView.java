package com.example.effector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dongjianye.effector.OutlineEffector;

import androidx.annotation.Nullable;

/**
 * @author dongjianye on 12/31/20
 */
public class KeyView extends TextView {

    public KeyView(Context context) {
        super(context);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    public KeyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    public KeyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    public KeyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
        setClipToOutline(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                new OutlineEffector((ViewGroup) getParent(), this).onDown(event.getX(), event.getY());
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}