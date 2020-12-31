package com.dongjianye.effector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.dongjianye.effector.internal.BlendManager;
import com.dongjianye.effector.internal.Blender;
import com.dongjianye.effector.internal.IEffector;

/**
 * @author dongjianye on 12/31/20
 */
public class OutlineEffector extends IEffector {

    /* renamed from: a */
    private float mAnimatedValue;

    /* renamed from: b */
    private float mAnimatedFraction;

    /* renamed from: c */
    private final Path mPath = new Path();

    /* renamed from: d */
    private final Path mAnimatedPath = new Path();

    /* renamed from: e */
    private final PathMeasure mPathMeasure = new PathMeasure();

    /* renamed from: f */
    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint;
    private final int[] mColors;
    private final ViewGroup mDrawTargetView;
    private final View mOutlineTargetView;

    public OutlineEffector(ViewGroup drawTargetView, View outlineTargetView) {
        mDrawTargetView = drawTargetView;
        mOutlineTargetView = outlineTargetView;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(-65536);
        paint.setStrokeWidth(10.0f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        mPaint = paint;
        mColors = new int[]{mDrawTargetView.f3583d.c(), mDrawTargetView.f3583d.c()};
    }

    private final Point getParentPosition() {
        final View view = this.mOutlineTargetView;
        if (view instanceof DummyEffectKeyView) {
            return new Point();
        }
        ViewParent parent = view.getParent();
        if (parent != null) {
            return new Point(((ViewGroup) parent).getLeft(), ((ViewGroup) parent).getTop());
        }
        throw new u("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public final ViewGroup getDrawTargetView() {
        return mDrawTargetView;
    }

    @Override // b.c.a.b.f.d.d
    public void onDown(float f2, float f3) {
        float dimension = this.mDrawTargetView.getResources().getDimension(R.dimen.honey_tea_corner_radius);
        Point position = getParentPosition();

        this.mPath.addRoundRect(
                (float) (position.x + this.mOutlineTargetView.getLeft()),
                (float) (position.y + this.mOutlineTargetView.getTop()),
                (float) (position.x + this.mOutlineTargetView.getRight()),
                (float) (position.y + this.mOutlineTargetView.getBottom()),
                dimension, dimension,
                Path.Direction.CW);

        this.mPathMeasure.setPath(this.mPath, false);
        this.mPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, (float) this.mDrawTargetView.getWidth(), this.mColors, (float[]) null, Shader.TileMode.CLAMP));

        final OutlineBlender blender = new OutlineBlender();

        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, this.mPathMeasure.getLength());
        ofFloat.setDuration(1000L);
        ofFloat.setInterpolator(a.sInstance.getPathInterpolator());
        ofFloat.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                BlendManager.removeBlender(getDrawTargetView(), blender);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                BlendManager.addBlender(getDrawTargetView(), blender);
            }
        });
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animatedValue = animation.getAnimatedValue();
                if (animatedValue != null) {
                    mAnimatedValue = ((Float) animatedValue).floatValue();
                    mAnimatedFraction = animation.getAnimatedFraction();
                    getDrawTargetView().invalidate();
                    return;
                }
            }
        });
        ofFloat.start();
    }

    private class OutlineBlender implements Blender {

        @Override
        public void blend(Canvas canvas) {
            mMatrix.reset();
            mMatrix.setRotate(360 * mAnimatedFraction);

            mPaint.getShader().setLocalMatrix(mMatrix);
            mPaint.setAlpha((int) (255 * (1 - mAnimatedFraction)));
            mPaint.setStrokeWidth(200 * mAnimatedFraction);

            mAnimatedPath.reset();
            mPathMeasure.getSegment(0.0f, mAnimatedValue, mAnimatedPath, true);

            canvas.drawPath(mAnimatedPath, mPaint);
        }
    }
}