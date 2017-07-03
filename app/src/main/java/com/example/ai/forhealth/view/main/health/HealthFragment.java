package com.example.ai.forhealth.view.main.health;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ai.forhealth.R;
import com.example.ai.forhealth.controller.Controller;
import com.example.ai.forhealth.model.health_knowledges.Bean.Bean;
import com.example.ai.forhealth.model.health_knowledges.Bean.Category;
import com.example.ai.forhealth.model.health_knowledges.Bean.ListInformation;
import com.example.ai.forhealth.model.health_knowledges.Bean.data;
import com.example.ai.forhealth.utils.AnimUtils;
import com.example.ai.forhealth.utils.JsonUtils;
import com.example.ai.forhealth.utils.Tools;
import com.example.ai.forhealth.view.main.health.adapter.HealthRVAdapter;
import com.example.ai.forhealth.view.mview.NumberPickerView;
import com.example.ai.forhealth.view.mview.banner.Banner;
import com.example.ai.forhealth.view.mview.banner.BannerAdapter;
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

public class HealthFragment extends Fragment {

    private String TAG = "healthFragment";

    private View view;

    //name为key ID为value
    private HashMap<String, String> category_ID = new HashMap<String, String>();

    private Controller controller = Controller.getInstance();

    private String[] categorys;

    private String currentCategory;

    private Dialog bottomDialog;

    public NumberPickerView picker;

    public Button ok;

    public Button cancle;

    @InjectView(R.id.health_banner)
    public Banner banner;

    @InjectView(R.id.health_fab)
    public FloatingActionButton fab;

    @InjectView(R.id.health_recycler_view)
    public RecyclerView recyclerView;

    @InjectView(R.id.health_progress_bar)
    public ProgressBar progressBar;

    @InjectView(R.id.health_framelayout)
    public FrameLayout frameLayout;

    @InjectView(R.id.health_bottom_view)
    public RelativeLayout bottomView;

    @InjectView(R.id.health_fab_current_category)
    public TextView bottomView_category;

    private List<data> list = new ArrayList<>();

    private HealthRVAdapter adapter;

    private int pageCount = 1;

    private View FailView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health, null);
        ButterKnife.inject(this, view);

        initBanner();

        initBottomDialogView();

        initRecyclerView();

        initBottomView();

        return view;

    }

    AnimatorSet backAnimatorSet;//这是显示头尾元素使用的动画
    AnimatorSet hideAnimatorSet;//这是隐藏头尾元素使用的动画

    private void animateHide()
    {
        //先清除其他动画
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            hideAnimatorSet = new AnimatorSet();
            //ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), -toolbar.getHeight());//将ToolBar隐藏到上面
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(bottomView, "translationY", bottomView.getTranslationY(), bottomView.getHeight());//将Button隐藏到下面
            ArrayList<Animator> animators = new ArrayList<>();
            //animators.add(headerAnimator);
            animators.add(footerAnimator);
            hideAnimatorSet.setDuration(150);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }
    }

    private void animateBack()
    {
        //先清除其他动画
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            backAnimatorSet = new AnimatorSet();
            //下面两句是将头尾元素放回初始位置。
            //ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", toolbar.getTranslationY(), 0f);
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(bottomView, "translationY", bottomView.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            //animators.add(headerAnimator);
            animators.add(footerAnimator);
            backAnimatorSet.setDuration(200);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }

    }

    private void initBottomView() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            float lastY = 0f;
            float currentY = 0f;
            //下面两个表示滑动的方向，大于0表示向下滑动，小于0表示向上滑动，等于0表示未滑动
            int lastDirection = 0;
            int currentDirection = 0;
            int touchSlop = 10;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastY = event.getY();
                        currentY = event.getY();
                        currentDirection = 0;
                        lastDirection = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float tmpCurrentY = event.getY();
                        if (Math.abs(tmpCurrentY - lastY) > touchSlop) {//滑动距离大于touchslop时才进行判断
                            currentY = tmpCurrentY;
                            currentDirection = (int) (currentY - lastY);
                            if (lastDirection != currentDirection) {
                                //如果与上次方向不同，则执行显/隐动画
                                if (currentDirection < 0) {
                                    animateHide();
                                } else {
                                    animateBack();
                                }
                            }
                            lastY = currentY;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //手指抬起的时候要把currentDirection设置为0，这样下次不管向哪拉，都与当前的不同（其实在ACTION_DOWN里写了之后这里就用不着了……）
                        currentDirection = 0;
                        lastDirection = 0;
                        break;
                }

                return false;
            }
        });
    }

    private void initBanner() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        FailView = inflater.inflate(R.layout.tip, null);

        new AsyncTask<Void, Void, List<data>>() {
            @Override
            protected List<data> doInBackground(Void... params) {

                //TODO 发布时取消注释，删除模拟部分即可即可
//                try {
//                    return controller.getHealth().queryListInformation(1, "21");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d("health", "doInBackground: " + e.getMessage());
//                    return null;
//                }
                //模拟
                List<data> datas = new ArrayList<data>();
                for (int i = 0; i < 20; i++) {
                    data da = new data();
                    da.title = "title"+i;
                    datas.add(da);
                }
                return datas;
            }

            @Override
            protected void onPostExecute(List<data> datas) {

                if (datas == null) ;
//                    ViewUtils.showToast(getContext(), "获取数据出错", MiuiToast.LENGTH_SHORT);
                else {
                    setBanner((ArrayList<data>) datas);
                }
            }
        }.execute();
    }

    /**
     * 显示底部弹出的对话框
     */
    private void initBottomDialogView() {
        String jsonString;
        Bean<ArrayList<Category>> bean = new Bean<ArrayList<Category>>();
        try {
            jsonString = Tools.readStream(Tools.getAssert(getContext(), "health_category"));
            bean = JsonUtils.deserialize(jsonString, new TypeToken<Bean<ArrayList<Category>>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "initFab: " + e.getMessage());
        }
        categorys = new String[bean.result.size()]; //13
        for (int i = 0; i < categorys.length; i++) {
            categorys[i] = bean.result.get(i).name;
            category_ID.put(bean.result.get(i).name, bean.result.get(i).id);
        }
        currentCategory = categorys[0];
        bottomView_category.setText(currentCategory);


        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_health_open_category, null);

        final String[] temp = new String[1];

        ok = (Button) view.findViewById(R.id.health_category_ok);
        cancle = (Button) view.findViewById(R.id.health_category_cancle);
        picker = (NumberPickerView) view.findViewById(R.id.health_category_number_picker);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp[0] == null) ; //第一次滑动时第一个是null的
                else if (currentCategory.equals(temp[0])) ;
                else {
                    currentCategory = temp[0];
                    bottomView_category.setText(currentCategory);

                    if (list.size() != 0 || !list.isEmpty())
                        list.clear();
                    adapter.notifyDataSetChanged();
                    adapter.tagsChange();
                    pageCount = 1;

                    loadData(1);
                }
                dismiss();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        picker.setListener(new NumberPickerView.OnValueChanged() {
            @Override
            public void onValueChanged(int position, Object defaultValue) {
                temp[0] = defaultValue.toString();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.show();
            }
        });

        picker.setColor(
                getResources().getColor(R.color.white_d_d),
                getResources().getColor(R.color.green_d)
        );
        picker.setSelection(0); //不调用会有ArrayIndexOutOfBoundsException:
        picker.setSelector((Object[]) categorys);

        setDialog(view);

    }

    /**
     * 调用RecycleView视图初始化
     */
    private void initRecyclerView() {
        initView();
        initListener();
    }

    /**
     * RecycleView视图初始化
     * 绑定没有数据的adapter
     */
    private void initView() {

        //创建一个LinearLayoutManager对象
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HealthRVAdapter(getContext(), recyclerView);

        adapter.setData(list); //第一次加载时再续绑定数据源
        recyclerView.setAdapter(adapter);

//        设置分隔线
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL_LIST));
//
//        //设置增加或删除条目的动画
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(pageCount); //pageCount == 1
    }

    /**
     * adapter item点击事件
     * 加载更多回调
     */
    private void initListener() {

        //加载更多回调监听
        adapter.setOnMoreDataLoadListener(new LoadMoreDataListener() {
            @Override
            public void loadMoreData() {
                pageCount++;
                //加入null值此时adapter会判断item的type
                list.add(null);
                adapter.notifyDataSetChanged();
                loadData(pageCount);

            }
        });

        adapter.setOnItemClickListener(new RecyclerOnItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Snackbar.make(view, "click pos=" + pos, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 开启异步任务从网络加载数据
     * @param page 所需加载的页码
     */
    private void loadData(final int page) {
        String id = category_ID.get(currentCategory);
        new AsyncTask<String, Void, ArrayList<data>>() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPreExecute() {
                //recyclerView.setVisibility(View.INVISIBLE);
                if (pageCount == 1) {
                    progressBar.setVisibility(View.VISIBLE);
                    showConnectFail(false);
                }
            }

            @Override
            protected ArrayList<data> doInBackground(String... params) {

                try {
                    //TODO 发布时取消注释，删除模拟部分即可即可
//                    return controller.getHealth().queryListInformation(page, params[0]);

                    //模拟
                    Bean<ListInformation> bean = new Bean<ListInformation>();
                    String jsonString = Tools.readStream(Tools.getAssert(getContext(), "health_11"));
                    bean = JsonUtils.deserialize(jsonString, new TypeToken<Bean<ListInformation>>() {
                    }.getType());
                    return bean.result.data;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(ArrayList<data> datas) {

                if (datas == null) {
                    if (pageCount == 1) {
                        showConnectFail(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (pageCount == 1) {
                        progressBar.setVisibility(View.INVISIBLE);
                        showConnectFail(false);
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                    show(datas);
                }
            }
        }.execute(id);
    }

    /**
     * 为list更新数据或第一次加载数据
     * @param datas 数据
     */
    private void show(ArrayList<data> datas) {

        if (pageCount == 1) {
            list.addAll(datas);
            adapter.notifyDataSetChanged();
            adapter.tagsChange();
        } else {
            //移除刷新的progressBar(最后一个)
            list.remove(list.size() - 1);
            adapter.notifyDataSetChanged();
            list.addAll(datas); //添加最新的数据-------------------
            adapter.notifyDataSetChanged();//更新数据-------------------?????????
            //数据更新的同时要更新LoadListImage的Tags
            adapter.tagsChange();
            adapter.setLoaded();//加载完成标记-------------------

        }
    }

    /**
     * 是否需要显示“加载失败界面”
     * @param bo true为需要显示
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showConnectFail(boolean bo) {
        TextView text = (TextView) FailView.findViewById(R.id.no_network_textView);
        ImageView imageI = (ImageView) FailView.findViewById(R.id.no_network_imageView);
        imageI.setImageResource(R.mipmap.tip_error);
        text.setText("抱歉，似乎出了点问题");
        Button button = (Button) FailView.findViewById(R.id.no_network_button);
        button.setText("重新加载");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeView(FailView);
                loadData(pageCount);
            }
        });

        if (bo) //需要显示
        {
            int in = frameLayout.indexOfChild(FailView);
            if (in < 0) { //大于0表示已经存在
                frameLayout.addView(FailView);
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_in);
                view.startAnimation(anim);
            }
        } else
            frameLayout.removeView(FailView);

    }

    /**
     * 底部弹出对话框
     * @return view
     */
    private void setDialog(View view) {
        if (bottomDialog == null) {
            bottomDialog = new Dialog(getContext(), R.style.custom_dialog);
            bottomDialog.setCanceledOnTouchOutside(true);
            //获取对话框的窗口，并设置窗口参数
            Window win = bottomDialog.getWindow();
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            // 不能写成这样,否则Dialog显示不出来
            // LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            //对话框窗口的宽和高
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //对话框窗口显示的位置
            params.x = 120;
            params.y = 100;
            win.setAttributes(params);
            //设置对话框布局
        }
        bottomDialog.setContentView(view);
    }

    /**
     * 隐藏对话框
     */
    public void dismiss() {
        //隐藏对话框之前先判断对话框是否存在，以及是否正在显示
        if (bottomDialog != null && bottomDialog.isShowing()) {
            bottomDialog.dismiss();
        }
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

                //TODO 发布时取消注释，删除模拟部分即可即可

//                Glide.with(getContext())
//                        .load(data.img)
//                        .placeholder(R.mipmap.loading_image)
//                        .error(R.mipmap.load_image_fail)
//                        .into(imageView);
                //模拟
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
