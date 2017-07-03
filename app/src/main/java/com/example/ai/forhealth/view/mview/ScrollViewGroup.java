package com.example.ai.forhealth.view.mview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 *
 */

public class ScrollViewGroup extends FrameLayout {

    private static final String TAG = ScrollViewGroup.class.getSimpleName();
    private Scroller mScroller;
    private int mHeight;
    private int mWidth;

    public ScrollViewGroup(Context context) {
        super(context);
        init();
    }

    public ScrollViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        Log.i(TAG, "height:" + mHeight + " ,width:" + mWidth);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void up() {
        int startX = 0;
        int startY = -mHeight;
        int dx = 0;
        int dy = mHeight;
        int duration = 1000;

        smoothScrollTo(startX, startY, dx, dy, duration);
    }

    public void down() {
        int startX = 0;
        int startY = 0;
        int dx = 0;
        int dy = -mHeight;
        int duration = 2000;

        smoothScrollTo(startX, startY, dx, dy, duration);
    }

    public void left() {
        int startX = mWidth;
        int startY = 0;
        int dx = -mWidth;
        int dy = 0;
        int duration = 2000;

        smoothScrollTo(startX, startY, dx, dy, duration);
    }

    public void right() {
        int startX = -mWidth;
        int startY = 0;
        int dx = mWidth;
        int dy = 0;
        int duration = 1000;

        smoothScrollTo(startX, startY, dx, dy, duration);
    }

    public void smoothScrollTo(int startX, int startY, int dx, int dy, int duration) {
        mScroller.startScroll(startX, startY, dx, dy, duration);
        invalidate();
    }

}
