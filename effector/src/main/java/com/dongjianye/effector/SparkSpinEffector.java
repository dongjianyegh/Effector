package com.dongjianye.effector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.View;

import com.dongjianye.effector.internal.BlendManager;
import com.dongjianye.effector.internal.Blender;
import com.dongjianye.effector.internal.DpUtils;
import com.dongjianye.effector.internal.IEffector;
import com.dongjianye.effector.internal.PathUtil;
import com.dongjianye.effector.internal.RandomUtil;
import com.dongjianye.effector.internal.TrigonometricFunctions;
import com.google.android.material.math.MathUtils;

import java.util.Random;

/**
 * @author dongjianye on 1/4/21
 */
public class SparkSpinEffector extends IEffector {

    private final int mAngleCnt = 5;
    private final int mAngleDegree = (360 / this.mAngleCnt);
    private final float mRadius = DpUtils.dp2pixel(100);

    private final float mRectSizeStart = DpUtils.dp2pixel(30);
    private final float mRectSizeEnd = DpUtils.dp2pixel(15);

    private float mAnimatedDegree;
    private float mAnimatedFraction;

    private final Camera mCamera = new Camera();
    private final Matrix mMatrix = new Matrix();

    private final float mCornerRadius = DpUtils.dp2pixel(10);
    private final float mRandomDegree = RandomUtil.random(0, 90);
    private final Paint mPaint;

    private final View mTargetView;
    private final View mKbdView;

    public SparkSpinEffector(View view, View kbdView) {
        this.mTargetView = view;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff0000ff);
        paint.setStyle(Paint.Style.FILL);
        this.mPaint = paint;
        mKbdView = kbdView;
    }

    private final Camera getCamera() {
        return mCamera;
    }

    private final Matrix getMatrix() {
        return mMatrix;
    }

    public final View getTargetView() {
        return this.mTargetView;
    }

    private void onBlend(Canvas canvas, float centerX, float centerY, int offsetAngel, float radius) {
        for (int cnt = 0; cnt < mAngleCnt; cnt++) {
            float x = centerX + TrigonometricFunctions.cos((this.mAngleDegree * cnt) - offsetAngel, radius);
            float y = centerY + TrigonometricFunctions.sin((this.mAngleDegree * cnt) - offsetAngel, radius);
            float lerp = MathUtils.lerp(this.mRectSizeStart, this.mRectSizeEnd, this.mAnimatedFraction);
            canvas.save();
            rotateZByCenter(canvas, x, y);
            canvas.drawRoundRect(x - lerp, y - lerp, x + lerp, y + lerp, mCornerRadius, mCornerRadius, this.mPaint);
            canvas.restore();
        }
    }

    private final void rotateZByCenter(Canvas canvas, float centerX, float centerY) {
        getCamera().save();
        getCamera().rotateZ(this.mRandomDegree + (this.mAnimatedFraction * 90.0f));
        getCamera().getMatrix(getMatrix());
        getCamera().restore();
        getMatrix().preTranslate(-centerX, -centerY);
        getMatrix().postTranslate(centerX, centerY);
        canvas.setMatrix(getMatrix());
    }

    @Override
    public void onDown(final float touchX, final float touchY) {
        final float kbdTouchX = touchX + mKbdView.getLeft();
        final float kbdTouchY = touchY + mKbdView.getTop();

        Paint paint = this.mPaint;
        paint.setShader(new RadialGradient(kbdTouchX, kbdTouchY, this.mRadius, new int[]{paint.getColor(), 0}, (float[]) null, Shader.TileMode.CLAMP));
        final int randomDegree = new Random().nextInt(360);

        final Blender blender = new Blender() {
            @Override
            public void blend(Canvas canvas) {
                mPaint.setAlpha(Math.round(MathUtils.lerp(125.0f, 0.0f, mAnimatedFraction)));
                final int i = Math.round(mAnimatedFraction * ((float) 360));
                onBlend(canvas, kbdTouchX, kbdTouchY, randomDegree - i, mAnimatedDegree);
                onBlend(canvas, kbdTouchX, kbdTouchY, randomDegree + mAngleDegree / 2 + i, mAnimatedDegree / 2);
            }
        };

        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, this.mRadius);
        ofFloat.setDuration(2000L);
        ofFloat.setInterpolator(PathUtil.getPathInterpolator());

        ofFloat.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                BlendManager.removeBlender(getTargetView(), blender);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                BlendManager.addBlender(getTargetView(), blender);
            }
        });

        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animatedValue = animation.getAnimatedValue();
                if (animatedValue != null) {
                    mAnimatedDegree = ((Float) animatedValue);
                    mAnimatedFraction = animation.getAnimatedFraction();
                    getTargetView().invalidate();
                }
            }
        });
        ofFloat.start();
    }
}