package com.example.ai.forhealth.app;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.utils.ViewUtils;
import com.example.ai.forhealth.view.main.express.ExpressFragment;
import com.example.ai.forhealth.view.main.health.HealthFragment;
import com.example.ai.forhealth.view.main.news.NewsFragment;
import com.example.ai.forhealth.view.main.other.other;
import com.example.ai.forhealth.view.main.train.TrainFragment;
import com.example.ai.forhealth.view.mview.MiuiToast;
import com.example.ai.forhealth.view.mview.VerticalViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RootActivity extends AppCompatActivity {

    @InjectView(R.id.app_bar_main_)
    public CoordinatorLayout coor;

    @InjectView(R.id.drawer_layout)
    public DrawerLayout drawer;

    @InjectView(R.id.drawer_drawer_close)
    public ImageButton drawerClose;

    @InjectView(R.id.app_bar_openDrawer)
    public ImageButton contentBt;

    @InjectView(R.id.left_listview)
    public ListView drawerInclude;

    @InjectView(R.id.left_bk_image)
    public ImageView drawerIncludeImage;

    @InjectView(R.id.content_main_viewPager)
    public VerticalViewPager viewPager;

    @InjectView(R.id.app_bar_my)
    public ImageButton app_bar_my;

    @InjectView(R.id.app_bar_text)
    public TextView app_bar_text;

    private List<String> title = new ArrayList<String>();

    //退出按钮
    private long firstExitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setDrawer();

        setDrawerInclude();

        setViewPager();

        initLisener();

        setMToolBar();

    }

    private void initLisener() {
        app_bar_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.showToast(RootActivity.this,"点击了My", MiuiToast.LENGTH_SHORT);
            }
        });
    }

    private void setMToolBar() {
        RelativeLayout app_bar = (RelativeLayout) findViewById(R.id.app_bar);
        ImageButton openDrawer = (ImageButton) app_bar.findViewById(R.id.app_bar_openDrawer);
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

    }

    /**
     * 设置ViewPager
     */
    private void setViewPager() {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        fragmentList.add(new TrainFragment());
        fragmentList.add(new HealthFragment());
        fragmentList.add(new ExpressFragment());
        fragmentList.add(new NewsFragment());
        fragmentList.add(new other());

        title.add("Query助手");
        title.add("健康资讯");
        title.add("快递查询");
        title.add("新闻头条");
        title.add("收藏");

        MainFragmentAdapter adapter = new MainFragmentAdapter(
                getSupportFragmentManager(),
                fragmentList,
                title
                );
        viewPager.setAdapter(adapter);


    }

    /**
     * drawer的listView数据来源
     * @return
     */
    private List<Map<String,Object>> getData()
    {
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        int[] resImage = {
                R.mipmap.train,
                R.mipmap.health,
                R.mipmap.express,
                R.mipmap.news,
                R.mipmap.collections
        };
        for (int i = 0; i < resImage.length; i++) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("main",resImage[i]);
            map.put("new",null);
            data.add(map);
        }
        return data;
    }

    /**
     * 设置drawer内包含的listView
     */
    private void setDrawerInclude() {

        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) drawerIncludeImage.getLayoutParams();

        SimpleAdapter adapter = new SimpleAdapter(
                RootActivity.this,
                getData(),
                R.layout.drawer_list_item,
                new String[]{"main","new"},
                new int[]{R.id.drawer_list_item_image,R.id.drawer_list_item_new}
        );

        drawerInclude.setAdapter(adapter);

        drawerInclude.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private float imagePrePos = 0f;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                params.height = view.getHeight();
                params.width = view.getWidth();

                drawerIncludeImage.setBackgroundResource(R.drawable.bk_drawer_include);
                drawerIncludeImage.setLayoutParams(params);

                float now = position*view.getHeight();
                ObjectAnimator.ofFloat(drawerIncludeImage,"translationY",imagePrePos,now).setDuration(100).start();
                imagePrePos = now;

                viewPager.setCurrentItem(position);

                app_bar_text.setText(title.get(position));

            }
        });

    }

    /**
     *设置拖拽出的视图
     */
    private void setDrawer() {

        final DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) coor.getLayoutParams();

        drawer.setScrimColor(Color.TRANSPARENT); //去除侧边栏部分阴影

        drawerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) drawerClose.getLayoutParams();

        final RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) contentBt.getLayoutParams();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                int ma = (int) (drawerView.getWidth() * slideOffset);//已显现部分得像素宽
                int a = (int) ((coor.getWidth()) * (1.0 - slideOffset)); //未显现部分得像素

                layoutParams.setMarginStart(a);
                drawerClose.setLayoutParams(layoutParams);//拽出的view里的返回按钮

                layoutParams2.setMarginStart(-ma);
                contentBt.setLayoutParams(layoutParams2);//被遮挡的view里的返回按钮（使其待在原地相对手机屏幕边界）

                params.setMarginStart(ma);
                params.setMarginEnd(-ma);
                coor.setLayoutParams(params); //被遮挡的view

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        long secondExitTime = System.currentTimeMillis();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            if (secondExitTime - firstExitTime > 2000 ) //
            {
                ViewUtils.showToast(RootActivity.this,"再次点击退出程序",MiuiToast.LENGTH_SHORT);
                firstExitTime = secondExitTime;
            }
            else
                super.onBackPressed();
        }
    }
}
