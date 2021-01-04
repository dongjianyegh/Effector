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
import com.dongjianye.effector.internal.BrownianMotion;
import com.dongjianye.effector.internal.IEffector;
import com.dongjianye.effector.internal.PathUtil;
import com.dongjianye.effector.internal.Vector3;

import java.util.ArrayList;

/**
 * @author dongjianye on 1/4/21
 */
public final class BlobSpreadEffector extends IEffector {
    private final float f3429a;
    private final int mBlobCnt;
    private final long mDuration;

    private final ArrayList<Paint> mPaints;
    private final ArrayList<BrownianMotion> mBrownianMotions;

    private float mAnimatedFraction;
    private float mAnimatedRadius;
    private final float mRadius;
    private final View mTargetView;
    private final View mKeyView;
    private float mScale;
    private float mCenterRatio;

    public BlobSpreadEffector(View view, View keyView, float scale, float centerRatio, int flag) {
        this(view, keyView, (flag & 2) != 0 ? 0.3f : scale, (flag & 4) != 0 ? 0.7f : centerRatio);
    }

    public BlobSpreadEffector(View view, View keyView, float scale, float centerRatio) {
        mTargetView = view;
        mKeyView = keyView;
        mScale = scale;
        mCenterRatio = centerRatio;
        f3429a = 0.2f;
        mBlobCnt = 3;
        mDuration = 1500L;
        mPaints = new ArrayList<>();
        mBrownianMotions = new ArrayList<>();
        mRadius = ((float) Math.min(this.mTargetView.getWidth(), this.mTargetView.getHeight())) * this.mScale;
    }

    private final int getColor() {
        float f2 = 0.0f;
        if (mAnimatedFraction < 0.3f) {
            f2 = interpolator(mAnimatedFraction, 0.0f, 0.3f, 0.0f, 1.0f);
        } else {
            f2 = interpolator(mAnimatedFraction, 0.3f, 1.0f, 1.0f, 0.0f);
        }
        return (int) (f2 * ((float) 255));
    }

    private static float interpolator(float f2, float f3, float f4, float start, float end) {
        return start + (((f2 - f3) / (f4 - f3)) * (end - start));
    }

    public final View getTargetView() {
        return this.mTargetView;
    }

    @Override
    public void onDown(final float touchX, final float touchY) {
        final float kbdTouchX = touchX + mKeyView.getLeft();
        final float kbdTouchY = touchY + mKeyView.getTop();

        final int color = 0xff0000ff;
        for (int i = 0; i < mBlobCnt; i++) {
            BrownianMotion motion = new BrownianMotion(new Vector3(mTargetView.getWidth() * this.mCenterRatio, this.mTargetView.getHeight() * this.mCenterRatio, 1000.0f));

            motion.a(this.f3429a);
            this.mBrownianMotions.add(motion);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setShader(new RadialGradient(mRadius / 2.0f, mRadius / 2.0f, Math.max(1.0f, mRadius),
                    new int[]{color, 0}, null, Shader.TileMode.CLAMP));
            mPaints.add(paint);
        }

        final Blender blender = new Blender() {
            @Override
            public void blend(Canvas canvas) {
                int save = canvas.save();
                canvas.translate(kbdTouchX - mRadius / ((float) 4), kbdTouchY - mRadius / (float) 3);
                final int color = getColor();
                int size = mBrownianMotions.size();
                for (int i = 0; i < size; i++) {

                    final int saveCount = canvas.save();

                    final BrownianMotion motion = mBrownianMotions.get(i);
                    motion.update();

                    Paint paint = mPaints.get(i);
                    paint.setAlpha(color);

                    canvas.translate(motion.getPosition().getX(), motion.getPosition().getY());
                    canvas.drawCircle(mAnimatedRadius / 2.0f, mAnimatedRadius / 2.0f, mAnimatedRadius, paint);
                    canvas.restoreToCount(saveCount);
                }
                canvas.restoreToCount(save);
            }
        };

        ValueAnimator ofFloat = ValueAnimator.ofFloat(mRadius / 3, mRadius);
        ofFloat.setInterpolator(PathUtil.getPathInterpolator());
        ofFloat.setDuration(this.mDuration);
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
                mAnimatedFraction = animation.getAnimatedFraction();
                Object animatedValue = animation.getAnimatedValue();
                if (animatedValue != null) {
                    mAnimatedRadius = ((Float) animatedValue);
                    getTargetView().invalidate();
                }
            }
        });
        ofFloat.start();
    }
}