package com.randomappsinc.aroundme.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.randomappsinc.aroundme.R;

public class SkeletonView extends View {

    private ValueAnimator colorAnimator;

    public SkeletonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final float[] from = new float[3];
        final float[] to = new float[3];

        Color.colorToHSV(context.getResources().getColor(R.color.gray_100), from);
        Color.colorToHSV(context.getResources().getColor(R.color.gray_300), to);

        colorAnimator = ValueAnimator.ofFloat(0, 1);
        colorAnimator.setDuration(context.getResources().getInteger(R.integer.skeleton_anim_length));
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);

        final float[] hsv  = new float[3];                  // transition color
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                // Transition along each axis of HSV (hue, saturation, value)
                hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

                setBackgroundColor(Color.HSVToColor(hsv));
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        colorAnimator.start();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        colorAnimator.cancel();
    }
}
