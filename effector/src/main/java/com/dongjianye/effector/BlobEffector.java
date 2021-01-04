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
public class BlobEffector extends IEffector {

    private final float f3416a;
    private final int mBlobCnt;
    private final long mDuration;
    private final ArrayList<Paint> mPaints;
    final ArrayList<BrownianMotion> mBrownianMotions;

    private float mAnimatedFraction;
    private float mAnimatedRadius;
    private final float mRadius;
    private final View mView;
    private final View mKeyView;

    private float mScale;
    private float mCenterRatio;

    public BlobEffector(View view, View keyView, float scale, float centerRatio, int flag) {
        this(view, keyView, (flag & 2) != 0 ? 0.3f : scale, (flag & 4) != 0 ? 0.5f : centerRatio);
    }

    public BlobEffector(View view, View keyView, float scale, float centerRatio) {
        mView = view;
        mKeyView = keyView;
        mScale = scale;
        mCenterRatio = centerRatio;
        f3416a = 0.2f;
        mBlobCnt = 5;
        mDuration = 1500;
        mPaints = new ArrayList<>();
        mBrownianMotions = new ArrayList<>();
        mRadius =Math.min(mView.getWidth(), mView.getHeight()) * mScale;
    }

    public final View getTargetView() {
        return this.mView;
    }

    @Override // b.c.a.b.f.d.d
    public void onDown(final float touchX, final float touchY) {
        // 只是一个配置，获取一个配置
        final float kbdTouchX = touchX + mKeyView.getLeft();
        final float kbdTouchY = touchY + mKeyView.getTop();

        final int color = 0xff0000ff;
        for (int cnt = 0; cnt < mBlobCnt; cnt++) {
            BrownianMotion brownianMotion = new BrownianMotion(new Vector3(mView.getWidth() * this.mCenterRatio, mView.getHeight() * this.mCenterRatio, 1000.0f));
            brownianMotion.a(this.f3416a);
            this.mBrownianMotions.add(brownianMotion);

            Paint paint = new Paint();
            paint.setAlpha(125);
            paint.setShader(new RadialGradient(this.mRadius / 2.0f, this.mRadius / 2.0f, Math.max(1.0f, this.mRadius),
                    new int[]{color, 0}, null, Shader.TileMode.CLAMP));
            this.mPaints.add(paint);
        }

        final Blender blender = new Blender() {
            @Override
            public void blend(Canvas canvas) {
                mAnimatedRadius = mRadius * (1 - mAnimatedFraction);
                int save = canvas.save();
                canvas.translate(kbdTouchX - mRadius / 4, kbdTouchY - mRadius / 4);
                final int alpha = (int) (mAnimatedFraction * ((float) 255));
                final int size = mBrownianMotions.size();
                for (int i = 0; i < size; i++) {
                    int save2 = canvas.save();
                    final BrownianMotion brownianMotion = mBrownianMotions.get(i);
                    brownianMotion.update();

                    final Paint paint = mPaints.get(i);
                    paint.setAlpha(alpha);

                    canvas.translate(brownianMotion.getPosition().getX(), brownianMotion.getPosition().getY());
                    canvas.drawCircle(mAnimatedRadius / 2.0f, mAnimatedRadius / 2.0f, mAnimatedRadius, paint);
                    canvas.restoreToCount(save2);
                }
                canvas.restoreToCount(save);
            }
        };

        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
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
                getTargetView().invalidate();
            }
        });
        ofFloat.start();
    }
}