package com.example.ai.forhealth.view.main.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.controller.Controller;
import com.example.ai.forhealth.model.juhe_news.bean.data;
import com.example.ai.forhealth.view.main.news.adapter.NewsAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 分类fragment视图
 */
public class SpecifiedNewsFragment extends Fragment {

    private Controller controller = Controller.getInstance();

    private View view;

    @InjectView(R.id.new_listView)
    public ListView listView;

    @InjectView(R.id.new_progressBar)
    public ProgressBar progressBar;

    @InjectView(R.id.news_frameLayout)
    public FrameLayout frameLayout;

    //对应新闻类别
    private String category_of_new;

    //加载失败界面是否已经显示 true为已经显示
    private boolean isFailShow = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_new, null);
        ButterKnife.inject(this, view);
        category_of_new = getArguments().getString("category");

        initView();

        return view;
    }

    public void initView() {

        new AsyncTask<Void, Void, ArrayList<data>>() {

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected ArrayList<data> doInBackground(Void... voids) {

                try {
                    return controller.getNews().getNewsList(category_of_new);
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showConnectFail(true);
                        }
                    });
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<data> newsBeen) {

                progressBar.setVisibility(View.INVISIBLE);
                showConnectFail(false);

                if (newsBeen == null)
                    return;

                NewsAdapter adapter = new NewsAdapter(
                        getContext(),
                        newsBeen,
                        listView);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getContext(), "click " + i, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.execute();

    }


    /**
     * 是否需要显示“加载失败界面”
     * @param bo true为需要显示
     */
    private void showConnectFail(boolean bo) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.tip, null);
        TextView text = (TextView) view.findViewById(R.id.no_network_textView);
        ImageView imageI = (ImageView) view.findViewById(R.id.no_network_imageView);
        imageI.setImageResource(R.mipmap.tip_error);
        text.setText("抱歉，似乎出了点问题");
        Button button = (Button) view.findViewById(R.id.no_network_button);
        button.setText("重新加载");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeView(view);
                isFailShow = false;
                initView();
            }
        });

        if (bo && !isFailShow) { // 加载出错 && 加载失败没有显示
            frameLayout.addView(view);
            Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_in);
            view.startAnimation(anim);
            isFailShow = true;
        }
       if (!bo && isFailShow)
       {
           frameLayout.removeView(view);
           isFailShow = false;
       }

    }


}
