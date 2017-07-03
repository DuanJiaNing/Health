package com.example.ai.forhealth.view.main.health.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.model.health_knowledges.Bean.data;
import com.example.ai.forhealth.utils.AnimUtils;
import com.example.ai.forhealth.utils.loadImage.LoadImages;
import com.example.ai.forhealth.utils.loadImage.PostExecuteBitmap;
import com.example.ai.forhealth.view.main.health.LoadMoreDataListener;
import com.example.ai.forhealth.view.main.health.RecyclerOnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ai on 2016/12/6.
 */

public class HealthRVAdapter extends RecyclerView.Adapter {

    private static final int VIEW_ITEM = 0;

    private static final int VIEW_PROG = 1;

    private int mStart, mEnd;

    private boolean isFirst = true;

    private final Context mContext;

    private final RecyclerView mRecyclerView;

    private List<data> mData;

    private final LayoutInflater inflater;

    private LoadImages mImageLoader;

    private boolean isLoading;

    private int totalItemCount;

    private int lastVisibleItemPosition;

    private ArrayList<String> tags = new ArrayList<>();

    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 5;

    public HealthRVAdapter(Context context, RecyclerView recyclerView) {

        mContext = context;
        inflater = LayoutInflater.from(context);
        mRecyclerView = recyclerView;

        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            //底部加载更多
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    Log.d("test", "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition);
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        //此时是刷新状态
                        if (mMoreDataListener != null)
                            mMoreDataListener.loadMoreData();
                        isLoading = true;

                    }
                }
            });


        }

    }

    public void setLoaded() {
        isLoading = false;
    }


    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_ITEM) {
            holder = new MyViewHolder(inflater.inflate(R.layout.fragment_health_rv_item, parent, false));
        } else {
            holder = new MyProgressViewHolder(inflater.inflate(R.layout.fragment_health_rv_footer, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            if (((MyViewHolder) holder).title != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String date = sdf.format(new Date(Long.valueOf(mData.get(position).time)));

                ((MyViewHolder) holder).time.setText(date);
                ((MyViewHolder) holder).title.setText(mData.get(position).title);
                ((MyViewHolder) holder).description.setText(mData.get(position).description);
                ((MyViewHolder) holder).visit.setText(mData.get(position).count + "访问");
                ((MyViewHolder) holder).keyworld.setText(mData.get(position).keywords);

                ((MyViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnitemClickListener.onClick(v, position);
                    }
                });

                ((MyViewHolder) holder).image.setTag(mData.get(position).img);

                /** 图片加载-----------------------------*/
                try {
                    //加载图片
                    // TODO PostExecuteBitmap回调
                    Bitmap bitmap = mImageLoader.loadSingleImage(position);
                    if (bitmap != null)
                        ((MyViewHolder) holder).image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /** 图片加载-----------------------------*/

                ((MyViewHolder) holder).collection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AnimUtils().startGeneralBtAnim(v);

                        Toast.makeText(mContext, "click "+position+"collection", Toast.LENGTH_SHORT).show();
                    }
                });
                ((MyViewHolder) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AnimUtils().startGeneralBtAnim(v);

                        Toast.makeText(mContext, "click "+position+"share", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } else if (holder instanceof MyProgressViewHolder) {
            if (((MyProgressViewHolder) holder).pb != null)
                ((MyProgressViewHolder) holder).pb.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        return mData.get(position) != null ? VIEW_ITEM : VIEW_PROG;

    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView mark;
        TextView title;
        TextView description;
        TextView visit;
        TextView keyworld;
        ImageView image;
        ImageButton collection;
        ImageButton share;

        View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            this.time = (TextView) view.findViewById(R.id.health_rv_time);
            this.mark = (TextView) view.findViewById(R.id.health_rv_mark);
            this.title = (TextView) view.findViewById(R.id.health_rv_title);
            this.description = (TextView) view.findViewById(R.id.health_rv_description);
            this.visit = (TextView) view.findViewById(R.id.health_rv_visit);
            this.keyworld = (TextView) view.findViewById(R.id.health_rv_key_world);
            this.image = (ImageView) view.findViewById(R.id.health_rv_image);
            this.collection = (ImageButton)view.findViewById(R.id.health_rv_collection);
            this.share = (ImageButton)view.findViewById(R.id.health_rv_share);
        }
    }

    public class MyProgressViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar pb;

        public MyProgressViewHolder(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.pb);
        }

    }

    //设置数据的方法
    public void setData(List<data> data) {
        mData = data;
        tagsChange();
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.load_image_fail);
        this.mImageLoader = new LoadImages(
                bitmap,
                tags);
    }

    //mData数据更新后应该同时手动调用此方法更新图片加载需要的tag（url）
    public void tagsChange() {
        tags.clear();
        for (int i = 0; i < mData.size(); i++) {
            tags.add(mData.get(i).img);
        }
    }

    private LoadMoreDataListener mMoreDataListener;

    //加载更多监听方法
    public void setOnMoreDataLoadListener(LoadMoreDataListener onMoreDataLoadListener) {
        mMoreDataListener = onMoreDataLoadListener;
    }

    private RecyclerOnItemClickListener mOnitemClickListener;

    //点击事件监听方法
    public void setOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
        mOnitemClickListener = onItemClickListener;
    }
}