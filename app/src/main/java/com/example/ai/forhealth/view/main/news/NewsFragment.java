package com.example.ai.forhealth.view.main.news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.controller.Controller;
import com.example.ai.forhealth.view.main.news.adapter.NewsFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 */

public class NewsFragment extends Fragment implements View.OnClickListener{

    private View view;

    private Controller controller = Controller.getInstance();

    @InjectView(R.id.main_tablayout)
    public TabLayout tabLayout;

    @InjectView(R.id.main_viewPager)
    public ViewPager pager;

    @InjectView(R.id.news_button_more)
    public Button more;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news,null);

        ButterKnife.inject(this,view);

        setViewPager();

        return view;
    }

    /**
     * 设置viewPager
     */
    private void setViewPager() {

        more.setOnClickListener(this);

        //tabLayout属性设置
        int select_color = getResources().getColor(R.color.green);
        tabLayout.setTabTextColors(Color.GRAY, select_color);
        //用来设置tab的，同时也要覆写  PagerAdapter 的 CharSequence getPageTitle(int position) 方法，要不然 Tab 没有 title

        List<String> titles = Arrays.asList(controller.getNews().getChannel());

        List<Fragment> fragments = new ArrayList<Fragment>();
//        //定义好要执行的异步任务
//        ArrayList<RxJavaExecuteTasks> tasks = new ArrayList<>();
//        tasks.add(new RxJavaExecuteTasks<result>(getActivity()) {
//            @Override
//            protected result onExecute() throws Exception {
//                Log.d("news", "onExecute: thread id="+Thread.currentThread().getName());
//                result res = null;
//                String jsonString="";
//                res = controller.getWeight().getData("male","156","56");
//
//                URL ur = new URL(
//                        "http://api.jisuapi.com/weight/bmi"+"?appkey="
//                                +"5171de6ae9fd57a6"
//                                +"&sex="
//                                +"male"
//                                +"&height="
//                                +"156"
//                                +"&weight="
//                                +"56");
//                URLConnection conn = ur.openConnection();
//                BufferedReader bufr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                while ( (line = bufr.readLine()) != null)
//                    jsonString += line;
//                Log.d("news", "onExecute: json="+jsonString);
//
////                res = new result();
////                res.bmi="kl";
////                res.danger="sad";
//                return res;
//            }
//
//            @Override
//            protected void oneTaskCompleted(result t) {
//
//                Log.d("news", "oneTaskCompleted: thread id="+Thread.currentThread().getId());
//
//                if (t == null)
//                    return;
//
//                String str = "stail loading...";
//                test_textView.setText(str);
//                test_textView.setText(t.toString());
//
//            }
//        });
//
//        //订阅任务
//        new RxJavaTasksSubscribe(getActivity(),tasks) {
//            @Override
//            protected void onCompleted() {
//                test_progressBar.setVisibility(View.INVISIBLE);
//                Log.d("news", "onCompleted: thread id="+Thread.currentThread().getName());
//            }
//
//            @Override
//            protected void onStart() {
//                Log.d("news", "onStart: ");
//                test_progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected void onError(Throwable e){
//                Log.d("news", "onError: "+e.getMessage());
//
//            }
//        }.execute();
        for (String categoey_of_new:
              titles){
            SpecifiedNewsFragment fragment = new SpecifiedNewsFragment();
            Bundle bu = new Bundle();
            bu.putString("category",categoey_of_new);
            fragment.setArguments(bu);
            fragments.add(fragment);
        }

        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(
                getActivity().getSupportFragmentManager(),
                fragments,
                titles);
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);
        //关联 TabLayout viewpager
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    public void showDialog()
    {
        PopupMenu popup = new PopupMenu(getContext(),more);
        String[] tit = controller.getNews().getChannel();
        for (int i = 0; i < tit.length; i++) {
            popup.getMenu().add(0,i,0,tit[i]);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                pager.setCurrentItem(item.getItemId(),true);
                return true;
            }
        });
        popup.show(); //showing popup menu
    }

    @Override
    public void onClick(View v) {
       switch(v.getId())
       {
           case R.id.news_button_more:
               showDialog();
       }
    }
}
