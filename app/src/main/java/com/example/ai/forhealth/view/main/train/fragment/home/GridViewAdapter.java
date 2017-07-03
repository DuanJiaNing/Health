package com.example.ai.forhealth.view.main.train.fragment.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.utils.AnimUtils;
import com.example.ai.forhealth.utils.Tools;

/**
 * Created by ai on 2016/12/30.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;

    private final int SIZE = 20;


    public GridViewAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return SIZE;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.fragment_train_f_home_gv_item,null);
        ImageView image = (ImageView) convertView.findViewById(R.id.train_home_gridView_item_image);
        image.setBackgroundColor(Tools.getRandomColor());

        new AnimUtils().startAlphaAnimOnTouch(image,1f,0.7f,200);

        return convertView;
    }
}
