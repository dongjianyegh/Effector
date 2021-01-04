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
import com.dongjianye.effector.internal.DpUtils;
import com.dongjianye.effector.internal.IEffector;
import com.dongjianye.effector.internal.PathUtil;

/**
 * @author dongjianye on 12/31/20
 */
public class OutlineEffector extends IEffector {
    private float mAnimatedValue;
    private float mAnimatedFraction;

    private final Path mPath = new Path();
    private final Path mAnimatedPath = new Path();

    private final PathMeasure mPathMeasure = new PathMeasure();

    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint;
    private final int[] mColors;
    private final ViewGroup mDrawTargetView;
    private final View mOutlineTargetView;

    public OutlineEffector(ViewGroup drawTargetView, View targetView) {
        mDrawTargetView = drawTargetView;
        mOutlineTargetView = targetView;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(10.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        mPaint = paint;
        mColors = new int[]{0xFFFF0000, 0xFF0000FF};
    }

    private Point getParentPosition() {
        final View view = this.mOutlineTargetView;
        ViewParent parent = view.getParent();
        if (parent != null) {
            return new Point(((ViewGroup) parent).getLeft(), ((ViewGroup) parent).getTop());
        } else {
            return null;
        }
    }

    public final ViewGroup getDrawTargetView() {
        return mDrawTargetView;
    }

    @Override
    public void onDown(final float touchX, final float touchY) {
        float dimension = DpUtils.dp2pixel(8);
        Point position = getParentPosition();

        mPath.addRoundRect(
                (float) (position.x + this.mOutlineTargetView.getLeft()),
                (float) (position.y + this.mOutlineTargetView.getTop()),
                (float) (position.x + this.mOutlineTargetView.getRight()),
                (float) (position.y + this.mOutlineTargetView.getBottom()),
                dimension, dimension,
                Path.Direction.CW);

        mPathMeasure.setPath(mPath, false);
        mPaint.setShader(new LinearGradient(0.0f, 0.0f, mDrawTargetView.getWidth(), mDrawTargetView.getHeight(), this.mColors, null, Shader.TileMode.CLAMP));

        final OutlineBlender blender = new OutlineBlender();

        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, mPathMeasure.getLength());
        ofFloat.setDuration(1000L);
        ofFloat.setInterpolator(PathUtil.getPathInterpolator());
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
                    mAnimatedValue = ((Float) animatedValue);
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