package com.dongjianye.effector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.View;

import com.dongjianye.effector.internal.BlendManager;
import com.dongjianye.effector.internal.Blender;
import com.dongjianye.effector.internal.IEffector;
import com.dongjianye.effector.internal.PathUtil;

/**
 * @author dongjianye on 1/4/21
 */
public class RingSpreadEffector extends IEffector {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final View mView;
    private final View mKeyView;

    public RingSpreadEffector(View keyboardView, View keyView) {
        mView = keyboardView;
        mKeyView = keyView;
    }

    public final View getTargetView() {
        return this.mView;
    }

    @Override
    public void onDown(final float touchX, final float touchY) {
        final float kbdTouchX = touchX + mKeyView.getLeft();
        final float kbdTouchY = touchY + mKeyView.getTop();

        final int color = 0xFF0000FF;
        final float radius = ((float) Math.hypot(mView.getWidth(), mView.getHeight())) / 2.0f;
        final Blender blender = new Blender() {
            @Override
            public void blend(Canvas canvas) {
                canvas.drawRect(0, 0, getTargetView().getWidth(), getTargetView().getHeight(), mPaint);
            }
        };

        ValueAnimator animator = ValueAnimator.ofFloat(-0.64f, 0.64f);
        animator.setInterpolator(PathUtil.getPathInterpolator());
        animator.setDuration(1800L);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                BlendManager.addBlender(getTargetView(), blender);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                BlendManager.removeBlender(getTargetView(), blender);
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animatedValue = animation.getAnimatedValue();
                if (animatedValue != null) {
                    float floatValue = ((Float) animatedValue);
                    mPaint.setShader(new RadialGradient(kbdTouchX, kbdTouchY, radius, new int[]{0, color, 0}, new float[]{0.0f, 0.35f + floatValue, floatValue + 0.64f}, Shader.TileMode.CLAMP));
                    mPaint.setAlpha((int) ((((float) 1) - animation.getAnimatedFraction()) * ((float) 255)));
                    getTargetView().invalidate();
                }
            }
        });
        animator.start();
    }
}