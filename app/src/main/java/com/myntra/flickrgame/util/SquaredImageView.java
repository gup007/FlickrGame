package com.myntra.flickrgame.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.myntra.flickrgame.R;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link SquaredImageView}
 */

public class SquaredImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int DURATION = 1000;

    private boolean showing = true;
    private AnimatorSet animatorSet = new AnimatorSet();

    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void loadImage(String imageUrl) {
        setTag(imageUrl);
        loadImage();
    }

    public void loadImage() {
        String imageUrl = (String) getTag();
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        ImageCache.load(getContext(), imageUrl).placeholder(R.drawable.ic_insert_photo)
                .error(R.drawable.ic_error)
                .fit()
                .into(this);
    }

    public boolean isShowing(){
        return showing;
    }

    public void show(Animator.AnimatorListener listener){
        ObjectAnimator animationStart = ObjectAnimator.ofFloat(this, "rotationY", 180.0f, 90f);
        animationStart.setDuration(DURATION >> 1);
        animationStart.setInterpolator(new LinearInterpolator());

        ObjectAnimator animationEnd = ObjectAnimator.ofFloat(this, "rotationY", 90f, 0f);
        animationEnd.setDuration(DURATION >> 1);
        animationEnd.setInterpolator(new LinearInterpolator());
        animatorSet.play(animationStart).before(animationEnd);
        animationStart.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                loadImage();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if(listener != null){
            animatorSet.addListener(listener);
        }
        animatorSet.start();
        showing = true;
    }

    public void show(){
        show(null);
    }

    public void hide(){
        hide(null);
    }

    public void hide(Animator.AnimatorListener listener){
        ObjectAnimator animationStart = ObjectAnimator.ofFloat(this, "rotationY", 0.0f, 90f);
        animationStart.setDuration(DURATION >> 1);
        animationStart.setInterpolator(new LinearInterpolator());

        ObjectAnimator animationEnd = ObjectAnimator.ofFloat(this, "rotationY", 90f, 180f);
        animationEnd.setDuration(DURATION >> 1);
        animationEnd.setInterpolator(new LinearInterpolator());
        animatorSet.play(animationStart).before(animationEnd);
        animationStart.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setImageResource(R.drawable.ic_insert_photo);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if(listener != null){
            animatorSet.addListener(listener);
        }
        animatorSet.start();
        showing = false;
    }
}
