package com.example.ai.forhealth.view.mview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;


import com.example.ai.forhealth.R;

import java.util.Arrays;


public class NumberPickerView extends View {

    private static final String TAG = "NumberPickerView";
    private Paint mTextPaint;//用于显示文字的画笔

    private int mMinValue;//最小值

    private int mMaxValue;//最大值

    private int mPageSize;//显示的个数

    private int mItemHeight;//组件的高度

    @DimenRes
    private int mTextSize;//字体大小

    private int mLastTouchY;//最后一次触摸的Y值


    private int mActivePointerId;//如果是多点触控，选择控制的点

    private Object[] mSelector;//显示内容的数组

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;

    private boolean mIsDragging;//判断是点击还是拖拽
    private int mTouchSlop;//最小的滑动距离
    private int mMaximumVelocity;//获得允许执行一个fling手势动作的最大速度值
    private int mMinimumVelocity;//获得允许执行一个fling手势动作的最小速度值


    @ColorInt
    private int mTextColorNormal;//文字默认颜色

    @ColorInt
    private int mTextColorSelected;//选中时文字的颜色


    private OnValueChanged mCallbackRef;

    private Rect itemRect;

    private int defaultValue = 0;


    public NumberPickerView(Context context) {
        super(context);
        init(context, null);
    }

    public NumberPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public void setColor(int normalColor,int selectColor)
    {
        mTextColorNormal = normalColor;
        mTextColorSelected = selectColor;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerView);
        mTextSize = (int) array.getDimension(R.styleable.NumberPickerView_numberTextSize, 80);
        mMaxValue = array.getInt(R.styleable.NumberPickerView_maxValue, 0);
        mMinValue = array.getInt(R.styleable.NumberPickerView_minValue, 0);
        mPageSize = array.getInt(R.styleable.NumberPickerView_numberPageSize, 5);
        mTextColorNormal = array.getColor(R.styleable.NumberPickerView_numberColorNormal, Color.GREEN);
        mTextColorSelected = array.getColor(R.styleable.NumberPickerView_numberColorSelected, Color.RED);
        array.recycle();

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.STROKE);

        if (mMinValue < mMaxValue) {
            mSelector = new Object[mMaxValue - mMinValue + 1];
            for (int selectorIndex = mMinValue; selectorIndex <= mMaxValue; selectorIndex++) {
                mSelector[selectorIndex - mMinValue] = selectorIndex;
            }
        }

        mOverScroller = new OverScroller(context, new DecelerateInterpolator());
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();//最小的滑动距离
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();//获得允许执行一个fling手势动作的最大速度值
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();//获得允许执行一个fling手势动作的最小速度值
        itemRect = new Rect();
        defaultValue = mPageSize / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ((mSelector == null || mSelector.length < 1))
            return;

        int width = getWidth();
        int height = mItemHeight;

        int itemHeight = getItemHeight();
        int textHeight = computeTextHeight();
        int centerY = getScrollY() + height / 2;

        int selectedPos = computePosition();
        for (int itemIndex = defaultValue; itemIndex < (mSelector.length + defaultValue); itemIndex++) {

            itemRect.set(0, itemIndex * itemHeight, width, itemIndex * itemHeight + itemHeight);
            // canvas.drawRect(itemRect, mTextPaint);

            if (itemIndex == selectedPos) {
                mTextPaint.setColor(mTextColorSelected);
            } else {
                mTextPaint.setColor(mTextColorNormal);
            }


            /**
             越靠近中间的文体越大。
             distance / (height / 2f) 算出的是递增的0-1之间的。
             1f - distance / (height / 2f) 将值变为递减1-0之间 。

             distance乘0.5可以确保scale不小于0.5
             */
            float distance = Math.abs(itemRect.centerY() - centerY) * 0.5f;
            float scale = 1f - distance / (height / 2f);
            float pivotY = itemRect.centerY();
            int alpha = (int) (scale * 255);

            mTextPaint.setAlpha(alpha);
            canvas.save();
            canvas.scale(scale, scale, itemRect.centerX(), pivotY);
            int y = (itemRect.top + itemRect.bottom - textHeight) / 2;

            canvas.drawText(mSelector[itemIndex - defaultValue] + "", itemRect.width() / 2, y, mTextPaint);
            canvas.restore();
        }

    /*    mTextPaint.setColor(Color.BLACK);
        canvas.drawLine(0, centerY, width, centerY, mTextPaint);*/

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null)
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int width = calculateSize(getSuggestedMinimumWidth(), lp.width, widthMeasureSpec);
        int height = calculateSize(getSuggestedMinimumHeight(), lp.height, heightMeasureSpec);

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mItemHeight = getHeight();
        }
    }

    /**
     * @param suggestedSize 最合适的大小
     * @param paramSize     配置的大小
     */
    private int calculateSize(int suggestedSize, int paramSize, int measureSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        switch (MeasureSpec.getMode(mode)) {
            case MeasureSpec.AT_MOST:
                if (paramSize == ViewGroup.LayoutParams.WRAP_CONTENT)
                    result = Math.min(suggestedSize, size);
                else if (paramSize == ViewGroup.LayoutParams.MATCH_PARENT)
                    result = size;
                else {
                    result = Math.min(paramSize, size);
                }
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                if (paramSize == ViewGroup.LayoutParams.WRAP_CONTENT || paramSize == ViewGroup.LayoutParams.MATCH_PARENT)
                    result = suggestedSize;
                else {
                    result = paramSize;
                }
                break;
        }

        return result;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        Log.i(TAG, "getSuggestedMinimumHeight");
        int suggested = super.getSuggestedMinimumHeight();
        if (mSelector != null && mSelector.length > 0 && mPageSize > 0) {
            Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
            int height = fontMetricsInt.descent - fontMetricsInt.ascent;//文字高度
            suggested = Math.max(suggested, height * mPageSize);
        }

        return suggested;
    }


    @Override
    protected int getSuggestedMinimumWidth() {
        Log.i(TAG, "getSuggestedMinimumWidth");
        int suggested = super.getSuggestedMinimumWidth();
        if (mSelector != null && mSelector.length > 0 && mPageSize > 0) {
            suggested = Math.max(suggested, computeMaximumWidth());
        }

        return suggested;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(event);
        }
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mOverScroller.isFinished())
                    mOverScroller.abortAnimation();

                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else
                    mVelocityTracker.clear();

                mVelocityTracker.addMovement(event);
                mActivePointerId = event.getPointerId(0);

                mLastTouchY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (mLastTouchY - event.getY(mActivePointerId));
                if (!mIsDragging && Math.abs(deltaY) > mTouchSlop) {
                    Log.i(TAG, "!mIsDragging");
                    //do something
                    final ViewParent parent = getParent();
                    if (parent != null)
                        parent.requestDisallowInterceptTouchEvent(true);//禁止事件拦截

                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                    mIsDragging = true;
                }

                if (mIsDragging) {
                    Log.i(TAG, "mIsDragging");
                    if (canScroll(deltaY))
                        scrollBy(0, deltaY);
                    mLastTouchY = (int) event.getY();
                }

                break;
            case MotionEvent.ACTION_UP:

                if (mIsDragging) {
                    mIsDragging = false;

                    final ViewParent parent = getParent();
                    if (parent != null)
                        parent.requestDisallowInterceptTouchEvent(false);

                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocity = (int) mVelocityTracker.getYVelocity(mActivePointerId);
                    if (Math.abs(velocity) > mMinimumVelocity) {
                        mOverScroller.fling(getScrollX(), getScrollY(), 0, -velocity, 0, 0,
                                getMinimumScrollY() - defaultVal, getMaximumScrollY() - defaultVal, 0, 0);
                        invalidateOnAnimation();
                    } else {
                        //align item;
                        adjustItem();
                    }

                    recyclerVelocityTracker();
                } else {
                    //click event
                    int y = (int) event.getY(mActivePointerId);
                    handlerClick(y);
                }

                break;
            case MotionEvent.ACTION_CANCEL:

                if (mIsDragging) {
                    adjustItem();
                    mIsDragging = false;
                }
                recyclerVelocityTracker();
                break;
        }
        return true;
    }

    private void recyclerVelocityTracker() {

        if (mVelocityTracker != null)
            mVelocityTracker.recycle();

        mVelocityTracker = null;
    }

    private void invalidateOnAnimation() {
        if (Build.VERSION.SDK_INT >= 16)
            postInvalidateOnAnimation();
        else
            invalidate();
    }

    /**
     * 点击滑动
     *
     * @param y 在view视图中点击的Y坐标
     */
    private void handlerClick(int y) {
        y = y + getScrollY();
        int position = y / getItemHeight();
        if (y >= 0 && position < mSelector.length + defaultValue) {
            Rect actualLoc = getLocationByPosition(position);
            int scrollY = actualLoc.top - getScrollY();
            mOverScroller.startScroll(getScrollX(), getScrollY(), 0, scrollY);
            invalidateOnAnimation();
        }
    }


    /**
     * 获取一个item位置，通过滚动正好将这个item放置在中间
     */
    private Rect getLocationByPosition(int position) {
        int scrollY = position * getItemHeight() + getMinimumScrollY();
        return new Rect(0, scrollY, getWidth(), scrollY + getItemHeight());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //判断mOverScroller是否执行完毕
        if (mOverScroller.computeScrollOffset()) {
            int x = mOverScroller.getCurrX();
            int y = mOverScroller.getCurrY();
            scrollTo(x, y);
            //通过重绘来不断调用computeScroll
            invalidate();
        } else if (!mIsDragging) {
            //align item
            adjustItem();
        }
    }

    int oldPos;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int position = computePosition();
        if (position != oldPos)
            if (mCallbackRef != null ) {
                int pos = position - defaultValue;
                // FIX
//                if (pos == -1)
//                    pos = 0;
                mCallbackRef.onValueChanged(position, mSelector[pos]);
                oldPos = position;
            }

    }

    public void smoothScrollTo(final int position) {
        if (position < 0 || mSelector == null || position > mSelector.length)
            return;
        Rect actualLoc = getLocationByPosition(position);
        int scrollY = actualLoc.top - getScrollY();
        mOverScroller.startScroll(getScrollX(), getScrollY(), 0, scrollY);
        invalidateOnAnimation();
    }

    private int defaultVal;

    public void setSelection(int pos) {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                defaultVal = -defaultValue * getItemHeight();
            }
        });
    }


    public void smoothScrollToValue(Object object) {
        if (mSelector == null)
            return;

        int pos = Arrays.binarySearch(mSelector, object);
        smoothScrollTo(pos);
    }

    /**
     * 调整item使对齐居中
     */
    private void adjustItem() {
        int position = computePosition();
        Rect rect = getLocationByPosition(position);
        int scrollY = rect.top - getScrollY();

        if (scrollY != 0) {
            mOverScroller.startScroll(getScrollX(), getScrollY(), 0, scrollY);
            invalidateOnAnimation();
        }
    }


    private int computePosition(int offset) {
        int topOffset = mItemHeight / 2;
        int scrollY = getScrollY() + topOffset + offset;
        return scrollY / getItemHeight();
    }


    /**
     * 计算当前显示的位置
     *
     * @return
     */
    public int computePosition() {
        return computePosition(0);
    }


    public void setSelector(Object... args) {
        mSelector = args;
        postInvalidate();
    }

    private boolean canScroll(int deltaY) {
        int scrollY = getScrollY() + deltaY + defaultVal;
        int top = getMinimumScrollY();
        int bottom = getMaximumScrollY();
        return scrollY >= top && scrollY <= bottom;
    }


    private int getMaximumScrollY() {
        return (mSelector.length - 1) * getItemHeight() + getMinimumScrollY();
    }

    private int getMinimumScrollY() {
        return -((mItemHeight - getItemHeight()) / 2);
    }


    /**
     * 计算可视区域内每个item的高度
     *
     * @return
     */
    public int getItemHeight() {
        return mItemHeight / mPageSize;
    }

    /**
     * 计算字符串的高度
     *
     * @return
     */
    private int computeTextHeight() {
        Paint.FontMetricsInt metricsInt = mTextPaint.getFontMetricsInt();
        return metricsInt.bottom + metricsInt.top;
    }

    /**
     * 计算字符串的最大高度
     *
     * @return
     */
    private int computeMaximumWidth() {
        int result = (int) mTextPaint.measureText("0000");
        int width = 0;
        for (int objIndex = 0; mSelector != null && objIndex < mSelector.length; objIndex++) {
            width = (int) mTextPaint.measureText(mSelector[objIndex].toString());
            if (width > result)
                result = width;
        }

        return result;
    }



    public void setListener(OnValueChanged valueChanged) {
        mCallbackRef = valueChanged;
    }

    public interface OnValueChanged {
        void onValueChanged(int position, Object defaultValue);
    }
}
