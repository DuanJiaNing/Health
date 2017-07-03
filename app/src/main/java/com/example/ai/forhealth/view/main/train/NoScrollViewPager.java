package com.example.ai.forhealth.view.main.train;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ai on 2016/12/15.
 */

public class NoScrollViewPager extends ViewPager {

    /**
     * 不可以滑动，但是可以setCurrentItem的ViewPager。
     */
        public NoScrollViewPager(Context context) {
            super(context);
        }

        public NoScrollViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent arg0) {
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent arg0) {
            return false;
        }
}
