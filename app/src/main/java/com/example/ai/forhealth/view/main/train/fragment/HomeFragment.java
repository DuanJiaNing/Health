package com.example.ai.forhealth.view.main.train.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ai.forhealth.R;
import com.example.ai.forhealth.controller.Controller;
import com.example.ai.forhealth.model.health_knowledges.Bean.Bean;
import com.example.ai.forhealth.model.health_knowledges.Bean.ListInformation;
import com.example.ai.forhealth.model.health_knowledges.Bean.data;
import com.example.ai.forhealth.utils.AnimUtils;
import com.example.ai.forhealth.utils.Tools;
import com.example.ai.forhealth.view.main.train.fragment.home.GridViewAdapter;
import com.example.ai.forhealth.view.mview.banner.Banner;
import com.example.ai.forhealth.view.mview.banner.BannerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by ai on 2016/12/14.
 */

public class HomeFragment extends Fragment {

    private View view;

    private Controller controller = Controller.getInstance();

    @InjectView(R.id.train_home_banner)
    public Banner banner;

    @InjectView(R.id.train_home_gridView)
    public GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_f_home,null);
        ButterKnife.inject(this,view);
        view.setTag(0);

        initBanner();

        initGridView();

        return view;
    }

    private void initGridView() {

        int length = 60+20;  //定义一个长度
        int size =20;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        //                    Log.d(TAG, "handleMessage: "+density);
        int gridviewWidth = (int) (size * (length + 10) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        //gridView.setHorizontalSpacing(15); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        gridView.setAdapter(new GridViewAdapter(getContext()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "click "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBanner() {

        new AsyncTask<Void, Void, List<data>>() {
            @Override
            protected List<data> doInBackground(Void... params) {

                try {
//                    return controller.getHealth().queryListInformation(1, "20");

                    String json = Tools.readStream( Tools.getAssert(getContext(),"health_11") );
                    Bean<ListInformation> bean = new Gson().fromJson(json,new TypeToken<Bean<ListInformation>>(){}.getType());
                    return bean.result.data;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("train", "doInBackground: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<data> datas) {

                if (datas == null)
                    return ;
                else {
                    setBanner((ArrayList<data>) datas);
                }
            }
        }.execute();
    }

    private void setBanner(ArrayList<data> datas) {
        //banner中显示20条太多，只选择前10条
        ArrayList<data> ldatas = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            ldatas.add(datas.get(i));

        BannerAdapter adapter = new BannerAdapter<data>(ldatas) {
            @Override
            protected void bindTips(TextView tv, data data) {
                tv.setText(data.title);
            }

            @Override
            public void bindImage(ImageView imageView, data data) {
//                Glide.with(getContext())
//                        .load(data.img)
//                        .placeholder(R.mipmap.loading_image)
//                        .error(R.mipmap.load_image_fail)
//                        .into(imageView);

                imageView.setBackgroundColor(Tools.getRandomColor());
            }
        };
        banner.setBannerAdapter(adapter);
        banner.notifiDataHasChanged(); //不调用会抛未初始化异常

        banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO 点击事件
                Toast.makeText(getContext(), "banner " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
