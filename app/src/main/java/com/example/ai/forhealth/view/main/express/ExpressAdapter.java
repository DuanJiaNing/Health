package com.example.ai.forhealth.view.main.express;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.model.express.bean.list;

import java.util.ArrayList;

/**
 *
 */
public class ExpressAdapter extends BaseAdapter{

    private ArrayList<list> mDataList = new ArrayList<>();

    private LayoutInflater mInflater;


    private Context context;

    private class viewHolder {
        TextView express_list_year;
        TextView express_list_hour;
        TextView express_list_stats;
        ImageView express_list_image;
    }

    public ExpressAdapter(Context context, ArrayList<list> data) {
        this.context = context;
        for (int i = data.size() - 1; i >= 0; i--) {
            this.mDataList.add(data.get(i));
        }
        this.mDataList = data;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        viewHolder holder = null;
        if (holder == null) {
            holder = new viewHolder();
            view = mInflater.inflate(R.layout.fragment_express_item, null);
            holder.express_list_year = (TextView) view.findViewById(R.id.express_list_year);
            holder.express_list_hour = (TextView) view.findViewById(R.id.express_list_hour);
            holder.express_list_stats = (TextView) view.findViewById(R.id.express_list_stats);
            holder.express_list_image = (ImageView) view.findViewById(R.id.express_list_image);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }

        if (mDataList.get(i).time.contains(" ") && mDataList.get(i).time.indexOf(" ") != 0) {
            String[] spi = mDataList.get(i).time.split(" ");
            holder.express_list_year.setText(spi[0]);
            holder.express_list_hour.setText(spi[1]);
        }
        else
        {
            holder.express_list_year.setText(" ");
            holder.express_list_hour.setText(" ");
        }
        holder.express_list_stats.setText(mDataList.get(i).status);

        if (i == 0) {
            int color = context.getResources().getColor(R.color.express_query);
            holder.express_list_year.setTextColor(color);
            holder.express_list_year.setBackgroundResource(R.drawable.express_list_item_year);
            holder.express_list_hour.setTextColor(color);
            holder.express_list_stats.setTextColor(color);
            holder.express_list_image.setImageResource(R.mipmap.express_last02);
        }
        else
            holder.express_list_image.setImageResource(R.mipmap.express_item);

        Animation anim = AnimationUtils.loadAnimation(context,R.anim.express_list_item);
        view.startAnimation(anim);
        return view;
    }

}
