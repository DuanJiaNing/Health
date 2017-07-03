package com.example.ai.forhealth.view.main.train;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.controller.Controller;
import com.example.ai.forhealth.model.health_knowledges.Bean.Bean;
import com.example.ai.forhealth.model.health_knowledges.Bean.Category;
import com.example.ai.forhealth.model.health_knowledges.Bean.ListInformation;
import com.example.ai.forhealth.model.health_knowledges.Bean.data;
import com.example.ai.forhealth.utils.AnimUtils;
import com.example.ai.forhealth.utils.JsonUtils;
import com.example.ai.forhealth.utils.Tools;
import com.example.ai.forhealth.utils.ViewUtils;
import com.example.ai.forhealth.view.main.train.adapter.SpacesItemDecoration;
import com.example.ai.forhealth.view.main.train.adapter.TrainFragmentPagerAdapter;
import com.example.ai.forhealth.view.main.train.adapter.TrainsRecyclerAdapter;
import com.example.ai.forhealth.view.main.train.fragment.HomeFragment;
import com.example.ai.forhealth.view.main.train.fragment.PriceFragment;
import com.example.ai.forhealth.view.main.train.fragment.StationsFragment;
import com.example.ai.forhealth.view.mview.MiuiToast;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ai on 2016/10/29.
 */

public class TrainFragment extends Fragment {

    private View view;

    //name为key ID为value
    private HashMap<String, String> category_ID = new HashMap<String, String>();

    private Controller controller = Controller.getInstance();

    private TrainsRecyclerAdapter adapter;

    private FullyLinearLayoutManager layoutManager;

    @InjectView(R.id.train_viewPager)
    public NoScrollViewPager viewPager;

    @InjectView(R.id.train_bt_more)
    public Button more;

    @InjectView(R.id.train_recycler_view)
    public RecyclerView recyclerView;

    @InjectView(R.id.train_show_category)
    public TextView showCategory;

    @InjectView(R.id.train_ib_home)
    public ImageButton home;
    @InjectView(R.id.train_ib_home__)
    public ImageView home__;

    @InjectView(R.id.train_ib_stations)
    public ImageButton stations;
    @InjectView(R.id.train_ib_stations__)
    public ImageView stations__;

    @InjectView(R.id.train_ib_price)
    public ImageButton price;
    @InjectView(R.id.train_ib_price__)
    public ImageView price__;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train, null);
        ButterKnife.inject(this, view);

        initTopIB();

        initCategoryData();

        initRecycle();

        initViewPager();

        return view;

    }

    private void topBottonClick(final int which) {

        if ((which) == viewPager.getCurrentItem())
            return;

        TranslateAnimation animation = new TranslateAnimation(
                0,0,
                0,view.getMeasuredHeight());
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewPager.setCurrentItem(which);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        viewPager.findViewWithTag(viewPager.getCurrentItem()).startAnimation(animation);

    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new StationsFragment());
        fragmentList.add(new PriceFragment());

        TrainFragmentPagerAdapter adapter = new TrainFragmentPagerAdapter(
                getActivity().getSupportFragmentManager(),
                fragmentList);

        viewPager.setAdapter(adapter);

    }

    private void initCategoryData() {
        String jsonString;
        Bean<ArrayList<Category>> bean = new Bean<ArrayList<Category>>();
        try {
            jsonString = Tools.readStream(Tools.getAssert(getContext(), "health_category"));
            bean = JsonUtils.deserialize(jsonString, new TypeToken<Bean<ArrayList<Category>>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bean.result.size(); i++)
            category_ID.put(bean.result.get(i).name, bean.result.get(i).id);
    }

    private void initRecycle() {
        //创建一个LinearLayoutManager对象
        layoutManager = new FullyLinearLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        recycleSetData("四季养生");
    }

    private void recycleSetData(final String name) {
        String id = category_ID.get(name);
        new AsyncTask<String, Void, ArrayList<data>>() {
            @Override
            protected void onPreExecute() {
                showCategory.setText("加载中...");
            }

            @Override
            protected ArrayList<data> doInBackground(String... params) {
                //TODO 发布时取消注释，删除模拟部分即可即可
//                try {
//                    return controller.getHealth().queryListInformation(2,params[0]);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }

                //模拟
                String jsonString;
                Bean<ListInformation> bean = new Bean<ListInformation>();
                try {
                    jsonString = Tools.readStream(Tools.getAssert(getContext(), "health_11"));
                    bean = JsonUtils.deserialize(jsonString, new TypeToken<Bean<ListInformation>>() {
                    }.getType());
                    return bean.result.data;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(ArrayList<data> datas) {
                if (datas != null) {
                    adapter = new TrainsRecyclerAdapter(getContext(), layoutManager, datas, recyclerView);
                    showCategory.setText(name);
                    recyclerView.setAdapter(adapter);
                } else
                    ViewUtils.showToast(getContext(), "获取数据出错啦", MiuiToast.LENGTH_SHORT);
            }
        }.execute(id);

    }

    /**
     * 顶部三个按钮点击动画和点击事件绑定
     */
    private void initTopIB() {

        final AnimUtils animUtils = new AnimUtils();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topBottonClick(0);
            }
        });
        home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        animUtils.startTrainTopBtAnim(home__, v, true);
                        break;
                    case MotionEvent.ACTION_UP:
                        animUtils.startTrainTopBtAnim(home__, v, false);
                        break;
                }
                return false;
            }
        });


        stations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topBottonClick(1);
            }
        });
        stations.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        animUtils.startTrainTopBtAnim(stations__, v, true);
                        break;
                    case MotionEvent.ACTION_UP:
                        animUtils.startTrainTopBtAnim(stations__, v, false);
                        break;
                }
                return false;
            }
        });


        price.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        animUtils.startTrainTopBtAnim(price__, v, true);
                        break;
                    case MotionEvent.ACTION_UP:
                        animUtils.startTrainTopBtAnim(price__, v, false);
                        break;
                }
                return false;
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topBottonClick(2);
            }
        });
    }

}
