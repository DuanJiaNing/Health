package com.example.ai.forhealth.view.main.train.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.model.health_knowledges.Bean.data;
import com.example.ai.forhealth.utils.AnimUtils;
import com.example.ai.forhealth.utils.Tools;
import com.example.ai.forhealth.utils.loadImage.LoadImages;
import com.example.ai.forhealth.view.main.train.FullyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by ai on 2016/12/13.
 */

public class TrainsRecyclerAdapter extends RecyclerView.Adapter<TrainsRecyclerAdapter.HealthView>{

    private ArrayList<data> datas;

    private LoadImages imagesLoader;

    private RecyclerView recyclerView;

    private ArrayList<String> tags = new ArrayList<>();

    private FullyLinearLayoutManager layoutManager;

    private Context context;

    public TrainsRecyclerAdapter(Context context, final FullyLinearLayoutManager layoutManager , ArrayList<data> list, RecyclerView recyclerView) {
        this.datas = list;
        this.context = context;
        this.layoutManager = layoutManager;
        this.recyclerView = recyclerView;

        for (int i = 0; i < list.size(); i++)
            tags.add(list.get(i).img);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.load_image_fail);
        imagesLoader = new LoadImages(bitmap,tags);

    }

    @Override
    public HealthView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_train_health_item, viewGroup, false);
        return new HealthView(view);
    }
    @Override
    public void onBindViewHolder(HealthView view, final int position) {
        view.title.setText(datas.get(position).title);

        String strs[] = datas.get(position).keywords.split(" ");
        String res = strs.length >= 2 ? strs[0]+" "+strs[1] : strs[0];
        view.key.setText(res);

        view.imageView.setTag(datas.get(position).img);

        //TODO 发布时取消注释并删除模拟即可
//        try {
//            //TODO PostExecuteBitmap回调
//            Bitmap bitmap = imagesLoader.loadSingleImage(datas.get(position).img);
//            if (bitmap != null)
//                view.imageView.setImageBitmap(bitmap);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        view.imageView.setBackgroundColor(Tools.getRandomColor());

        new AnimUtils().startAlphaAnimOnTouch(view.view,0f,0.5f,200); // 点击动画
        view.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click "+position, Toast.LENGTH_SHORT).show();
            }
        });
        view.bottom.setBackgroundColor(Tools.getRandomColor_d());
        //不能纵向滚动，因而滚动事件无法监听 视图绑定只会调用一次，一次就把所有20个item全加载了

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class HealthView extends  RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView key;
        View view;
        LinearLayout bottom;

        public HealthView(View itemView){
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.train_health_image );
            title= (TextView) itemView.findViewById(R.id.train_health_title);
            key= (TextView) itemView.findViewById(R.id.train_health_key);
            bottom = (LinearLayout) itemView.findViewById(R.id.train_health_bottom);
            view = itemView.findViewById(R.id.train_health_top);
            view.setAlpha(0);
        }

    }

}
