/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ai.forhealth.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

/**
 *
 */
public class AnimUtils {

    private void openHealthCategory(ArrayList<View> views) {
        for (int i = 0; i < views.size(); i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(
                    views.get(i),
                    "translationX",
                    0f,
                    i * -100).setDuration(400);
            animator.setInterpolator(new OvershootInterpolator());
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                    views.get(i),
                    "rotation",
                    0f,
                    360f).setDuration(400);
            AnimatorSet set = new AnimatorSet();
            set.play(animator).with(animator1);
            //set.setStartDelay(500);
            set.start();
        }
    }

    /**
     * 开始一个view alpha值从0到0.5结束时要变回动画
     * @param view itemView
     */
    public void startAlphaAnimOnTouch(View view,float from,float to,int duration) {

        final AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorPress = ObjectAnimator.ofFloat(view, "alpha", from, to);
        final ObjectAnimator animatorUP = ObjectAnimator.ofFloat(view, "alpha", to, from);
        animatorPress.setDuration(duration);
        animatorUP.setDuration(duration);
        set.play(animatorPress).before(animatorUP);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    set.start();
                return false;
            }
        });


    }

    /**
     * train 顶部的三个按钮的点击动画
     * @param IV 底图
     * @param IB 按钮
     * @param bo 按下为true 抬起为false
     */
    public void startTrainTopBtAnim(View IV, View IB, boolean bo) {
        IV.setVisibility(View.VISIBLE);
        float iv_x_f;
        float iv_y_f;
        float ib_x_f;
        float ib_y_f;
        float iv_x_t;
        float iv_y_t;
        float ib_x_t;
        float ib_y_t;

        if (bo) //down
        {
            iv_x_f = 0f;
            iv_y_f = 0f;
            iv_x_t = 1f;
            iv_y_t = 1f;

            ib_y_f = 1f;
            ib_x_f = 1f;
            ib_x_t = 0.8f;
            ib_y_t = 0.8f;

        } else //up
        {

            iv_x_f = 1f;
            iv_y_f = 1f;
            iv_x_t = 0f;
            iv_y_t = 0f;

            ib_y_f = 0.8f;
            ib_x_f = 0.8f;
            ib_x_t = 1f;
            ib_y_t = 1f;
        }

        AnimatorSet animatorSet_IV = new AnimatorSet();//组合动画
        animatorSet_IV.setInterpolator(new OvershootInterpolator());

        AnimatorSet animatorSet_IB = new AnimatorSet();//组合动画
        animatorSet_IB.setDuration(700);
        animatorSet_IB.setDuration(900);

        ObjectAnimator animator_IV_X = ObjectAnimator.ofFloat(IV, "scaleX", iv_x_f, iv_x_t);
        ObjectAnimator animator_IV_Y = ObjectAnimator.ofFloat(IV, "scaleY", iv_y_f, iv_y_t);
        ObjectAnimator animator_IB_X = ObjectAnimator.ofFloat(IB, "scaleX", ib_x_f, ib_x_t);
        ObjectAnimator animator_IB_Y = ObjectAnimator.ofFloat(IB, "scaleY", ib_y_f, ib_y_t);

        animatorSet_IV.play(animator_IV_X).with(animator_IV_Y);//两个动画同时开始
        animatorSet_IV.play(animator_IB_X).with(animator_IB_Y);//两个动画同时开始
        animatorSet_IV.start();
        animatorSet_IB.start();
    }

    /**
     * train 开始一个按钮的点击动画
     * @param button 按钮
     */
    public void startGeneralBtAnim(final View button) {

        ScaleAnimation animation_start = new ScaleAnimation(1.0f,1.3f,1.0f,1.3f);
        button.startAnimation(animation_start);
        final ScaleAnimation animation_recover = new ScaleAnimation(1.3f,1.0f,1.3f,1.0f);
        animation_start.setDuration(150);
        animation_recover.setDuration(150);
        animation_start.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.startAnimation(animation_recover);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 开始一个透明度变化动画
     * @param view 控件
     * @param from 起始透明度
     * @param to 终止透明度
     * @param duration 时长
     */
    public void startAlphaAnim(View view, float from, float to, long duration)
    {
        AlphaAnimation animation = new AlphaAnimation(from, to);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

}
