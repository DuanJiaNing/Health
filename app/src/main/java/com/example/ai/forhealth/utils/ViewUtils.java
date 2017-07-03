package com.example.ai.forhealth.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.view.mview.MiuiToast;

/**
 * Created by ai on 2016/12/1.
 */

final public class ViewUtils {


    public static void showToast(Context context, String str, boolean showTime)
    {
        MiuiToast toast = new MiuiToast(context,str,showTime);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.toask_base_view,null);
        TextView textView = (TextView) v.findViewById(R.id.toask_base_textView);
        textView.setText(str);
        toast.setView(v);
        toast.show();
    }
}
