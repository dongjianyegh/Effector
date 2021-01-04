package com.example.effector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dongjianye.effector.BlobEffector;
import com.dongjianye.effector.BlobSpreadEffector;
import com.dongjianye.effector.OutlineEffector;
import com.dongjianye.effector.RingSpreadEffector;
import com.dongjianye.effector.SparkEffector;
import com.dongjianye.effector.SparkSpinEffector;

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
                if ("A".equals(getText())) {
                    new OutlineEffector((ViewGroup) getParent(), this).onDown(event.getX(), event.getY());
                } else if ("B".equals(getText())) {
                    new RingSpreadEffector((ViewGroup) getParent(), this).onDown(event.getX(), event.getY());
                } else if ("C".equals(getText())) {
                    new SparkEffector((ViewGroup) getParent(), this).onDown(event.getX(), event.getY());
                } else if ("D".equals(getText())) {
                    new SparkSpinEffector((ViewGroup) getParent(), this).onDown(event.getX(), event.getY());
                } else if ("E".equals(getText())) {
                    new BlobEffector((ViewGroup) getParent(), this, 0, 0, 6).onDown(event.getX(), event.getY());
                } else if ("F".equals(getText())) {
                    new BlobSpreadEffector((ViewGroup) getParent(), this, 0, 0, 6).onDown(event.getX(), event.getY());
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}