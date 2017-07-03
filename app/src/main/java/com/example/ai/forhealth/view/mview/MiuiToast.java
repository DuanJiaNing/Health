package com.example.ai.forhealth.view.mview;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ai.forhealth.R;

public class MiuiToast {


	private WindowManager mWdm;
	private View mToastView;
	private WindowManager.LayoutParams mParams;
	private Timer mTimer;
	private boolean mShowTime;
	private boolean mIsShow;
	
	public static final boolean LENGTH_LONG = true;
	public static final boolean LENGTH_SHORT = false;

	public MiuiToast(Context context, String text, boolean showTime ){
		mShowTime = showTime;
		mIsShow = false;
		mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mToastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).getView();
		mTimer = new Timer();
		setParams();
	}

	
	private void setParams() {
		mParams = new WindowManager.LayoutParams();
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
		mParams.format = PixelFormat.TRANSLUCENT;  
		mParams.windowAnimations = R.style.anim_view;
		mParams.type = WindowManager.LayoutParams.TYPE_TOAST;  
		mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  
	            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  
	            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		mParams.gravity = Gravity.CENTER_HORIZONTAL;
		mParams.y = 250;
	}


	public static MiuiToast MakeText(Context context, String text, boolean showTime) {
		MiuiToast result = new MiuiToast(context, text, showTime);
		return result;
	}
	
	public void show(){
		if(!mIsShow){
			mIsShow = true;
			mWdm.addView(mToastView, mParams);
			mTimer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	mWdm.removeView(mToastView);
	            	mIsShow = false;
	            }
	        }, (long)(mShowTime ? 3500 : 2000));
		}
	}

	public void setView(View view)
	{
		mToastView = view;
	}
	
	public void cancel(){
		if(mTimer == null){
			mWdm.removeView(mToastView);
			mTimer.cancel();
		}
		mIsShow = false;
	}
}
