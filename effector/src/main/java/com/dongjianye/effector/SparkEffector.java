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

/**
 * @author dongjianye on 1/4/21
 */
public class SparkEffector extends IEffector {

    private final int mAngleCount = 5;
    private final int mAngleDegree = (360 / this.mAngleCount);
    private final float mRadius = DpUtils.dp2pixel(100);
    private float mAnimatedRadius;

    private final float mRectSizeStart = DpUtils.dp2pixel(30);
    private final float mRectSizeEnd = DpUtils.dp2pixel(15);
    private float mAnimatedFraction;

    private final Camera mCamera = new Camera();
    private final Matrix mMatrix = new Matrix();

    private final float mCornerRadius = DpUtils.dp2pixel(10);

    private final Paint mPaint;
    private final View mTargetView;
    private final View mKeyView;
    private final float mRandomDegree = RandomUtil.random(0, 90);

    public SparkEffector(View view, View keyView) {
        this.mTargetView = view;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff0000ff);
        paint.setStyle(Paint.Style.FILL);
        this.mPaint = paint;
        mKeyView = keyView;
    }

    private Camera getCamera() {
        return mCamera;
    }

    private Matrix c() {
        return mMatrix;
    }

    public final View getTargetView() {
        return this.mTargetView;
    }

    /* access modifiers changed from: public */
    private void onBlend(Canvas canvas, float centerX, float centerY, int offsetAngle, float radius) {
        for (int cnt = 0; cnt < mAngleCount; cnt++) {
            float x = centerX + TrigonometricFunctions.cos(mAngleDegree * cnt - offsetAngle, radius);
            float y = centerY + TrigonometricFunctions.sin(mAngleDegree * cnt - offsetAngle, radius);

            final float rectSize = MathUtils.lerp(this.mRectSizeStart, this.mRectSizeEnd, this.mAnimatedFraction);

            canvas.save();
            rotateZByCenter(canvas, x, y);
            canvas.drawRoundRect(x - rectSize, y - rectSize, x + rectSize, y + rectSize, mCornerRadius, mCornerRadius, this.mPaint);
            canvas.restore();
        }
    }

    private final void rotateZByCenter(Canvas canvas, float centerX, float centerY) {
        getCamera().save();
        getCamera().rotateZ(this.mRandomDegree + (this.mAnimatedFraction * 90.0f));
        getCamera().getMatrix(c());
        getCamera().restore();
        c().preTranslate(-centerX, -centerY);
        c().postTranslate(centerX, centerY);
        canvas.setMatrix(c());
    }

    @Override // b.c.a.b.f.d.d
    public void onDown(final float touchX, final float touchY) {
        final float kbdTouchX = mKeyView.getLeft() + touchX;
        final float kbdTouchY = mKeyView.getTop() + touchY;

        Paint paint = this.mPaint;
        paint.setShader(new RadialGradient(kbdTouchX, kbdTouchY, this.mRadius, new int[]{paint.getColor(), 0}, null, Shader.TileMode.CLAMP));

        final int offsetAngle = RandomUtil.random(0, 90);
        final Blender blender = new Blender() {
            @Override
            public void blend(Canvas canvas) {
                mPaint.setAlpha(Math.round(MathUtils.lerp(125.0f, 0.0f, mAnimatedFraction)));
                onBlend(canvas, kbdTouchX, kbdTouchY, offsetAngle, mAnimatedRadius);
                onBlend(canvas, kbdTouchX, kbdTouchY, offsetAngle + mAngleDegree / 2, mAnimatedRadius /2);
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
                    mAnimatedRadius = ((Float) animatedValue);
                    mAnimatedFraction = animation.getAnimatedFraction();
                    getTargetView().invalidate();
                }
            }
        });
        ofFloat.start();
    }

}