package com.example.ai.forhealth.view.main.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.model.juhe_news.bean.data;
import com.example.ai.forhealth.utils.AnimUtils;
import com.example.ai.forhealth.utils.BitmapUtil;
import com.example.ai.forhealth.utils.loadImage.LoadImages;
import com.example.ai.forhealth.utils.loadImage.PostExecuteBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NewsAdapter extends BaseAdapter {

    private List<data> mDataList;

    private LayoutInflater mInflater;

    private LoadImages mImageLoader;

    private ListView mListView;

    private class viewHolder {
        TextView title;
        TextView date;
        TextView author_name;
        ImageView pic;
    }

    public NewsAdapter(Context context, List<data> data,ListView listView) {

        this.mListView = listView;
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            tags.add(data.get(i).thumbnail_pic_s);
        }

        this.mDataList = data;

        this.mInflater = LayoutInflater.from(context);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.loading_image);
        final Bitmap fialBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.load_image_fail);
        this.mImageLoader = new LoadImages(bitmap, tags);
        mImageLoader.initAsyncTaskCallback(new PostExecuteBitmap() {
            @Override
            protected void postExecute(Bitmap bitmap,int index,String url) {
                //判断当前下载好的图片要放置的ImageView对应的视图当前是否正在显示
                if (index >= mListView.getFirstVisiblePosition() &&
                        index <= mListView.getLastVisiblePosition()
                        )
                {
                    //此方法是异步调用的所以找到放置图片的控件时：
                    //1.先判断item是否在显示（否则会报空指针异常）
                    //2.再找到要应用图片的控件
                    //3.mListView.getChildAt(index - mListView.getFirstVisiblePosition())
                    ImageView imageView = (ImageView) mListView.getChildAt(index - mListView.getFirstVisiblePosition()).findViewById(R.id.juhe_news_it_pic);
                    if (bitmap == null) //下载图片失败
                    {
                        imageView.setImageBitmap(fialBitmap);
                    }
                    else {
                        imageView.setImageBitmap(BitmapUtil.toRoundBitmap(bitmap));
                        new AnimUtils().startAlphaAnim(
                                imageView,
                                0.0f,
                                1.0f,
                                500);
                    }
                }
            }
        });


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
    public View getView(int i, View view, final ViewGroup viewGroup) {

        viewHolder holder = null;
        if (holder == null) {
            holder = new viewHolder();
            view = mInflater.inflate(R.layout.fragment_news_item, null);
//            view = mInflater.inflate(R.layout.item,null);
            holder.author_name = (TextView) view.findViewById(R.id.juhe_news_it_author_name);
            holder.title = (TextView) view.findViewById(R.id.juhe_news_it_title);
            holder.date = (TextView) view.findViewById(R.id.juhe_news_it_date);
            holder.pic = (ImageView) view.findViewById(R.id.juhe_news_it_pic);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }
        holder.author_name.setText(mDataList.get(i).author_name);

        holder.title.setText(mDataList.get(i).title);

        String da = mDataList.get(i).date;
        da = da.substring(10);
        holder.date.setText(da);

        /**图片加载---------------------------------------------------------------------------------*/
        try {
            Bitmap bitmap = mImageLoader.loadSingleImage(i);
            if (bitmap != null)
                holder.pic.setImageBitmap(BitmapUtil.toRoundBitmap(bitmap));

        } catch (Exception e) {
            e.printStackTrace();
        }
        /**图片加载----------------------------------------------------------------------------------*/

        return view;
    }
}
