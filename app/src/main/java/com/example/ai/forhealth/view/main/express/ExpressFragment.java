package com.example.ai.forhealth.view.main.express;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ai.forhealth.R;
import com.example.ai.forhealth.controller.Controller;
import com.example.ai.forhealth.model.express.bean.CompanyNo;
import com.example.ai.forhealth.model.express.bean.ExpressStatus;
import com.example.ai.forhealth.utils.CharUtils;
import com.example.ai.forhealth.utils.Tools;
import com.example.ai.forhealth.utils.ViewUtils;
import com.example.ai.forhealth.view.mview.MiuiToast;
import com.example.ai.forhealth.view.mview.PickerView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ai on 2016/10/29.
 */

public class ExpressFragment extends Fragment {

    private ArrayList<CompanyNo> companyNos = new ArrayList<>();

    private ArrayList<String> companyNames = new ArrayList<>();

    private ArrayList<String> companyPhones = new ArrayList<>();

    private ArrayList<String> companyTypes = new ArrayList<>();

    private ArrayList<String> pickerData = new ArrayList<>();

    private Controller controller = Controller.getInstance();

    private static final int DEFAULT_PICKER_SELECT = 10;

    private View view;

    @InjectView(R.id.express_pickerView)
    public PickerView pickerView;

    @InjectView(R.id.express_query)
    public Button query;

    @InjectView(R.id.express_company_call)
    public ImageButton companyCall;

    @InjectView(R.id.express_input_ticket_num)
    public EditText input;

    @InjectView(R.id.express_company_name)
    public TextView express_company_name;

    @InjectView(R.id.express_company_type)
    public TextView express_company_type;

    @InjectView(R.id.express_company_tel)
    public TextView express_company_tel;

    @InjectView(R.id.express_company_stats)
    public TextView express_company_stats;

    @InjectView(R.id.express_pickerView_auto)
    public Button express_pickerView_auto;

    @InjectView(R.id.express_listView)
    public ListView listView;

    @InjectView(R.id.express_list_progressBar)
    public ProgressBar express_list_progressBar;

    @InjectView(R.id.express_frameLayout)
    public FrameLayout frameLayout;

    private View viewI;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_express, null);
        ButterKnife.inject(this, view);

        initTipView();

        initView();

        showConnectFail(true);

        initPant(null, 0);

        // TODO 从本地json文件获得ExpressCompanyNo（耗时）
        companyNos = Tools.getExpressCompanyNos(getContext());
        initPickerData();
        setPickerView(null);

        return view;

    }

    /**
     * 获得PickerView要填充的数据
     *
     * @return 数据
     */
    private void initPickerData() {
        if (companyNos == null)
            pickerData.add("自动识别");
        else {
            for (int i = 0; i < companyNos.size(); i++) {
                companyNames.add(companyNos.get(i).name);
                companyPhones.add(companyNos.get(i).tel);
                companyTypes.add(companyNos.get(i).type);
                pickerData.add(companyNos.get(i).name);
            }
            pickerData.add(DEFAULT_PICKER_SELECT, "自动识别");
        }
    }

    /**
     * 查询按钮点击事件逻辑处理
     */
    private void executeQuery() {

        String type = "auto";
        String number = input.getText().toString();
        if (number.equals(""))
            ViewUtils.showToast(getContext(), "请输入订单号", MiuiToast.LENGTH_SHORT);
        else {

            new AsyncTask<String, Void, ExpressStatus>() {

                @Override
                protected void onPreExecute() {
                    express_list_progressBar.setVisibility(View.VISIBLE);
                    viewI.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }

                @Override
                protected ExpressStatus doInBackground(String... params) {
                    try {
                        return controller.getExpress().getDetail(params[0], params[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("express", "run: " + e.getMessage());
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(ExpressStatus status) {
                    express_list_progressBar.setVisibility(View.INVISIBLE);

                    if (status == null) {
                        showConnectFail(true);
                        ViewUtils.showToast(getContext(), "抱歉，查询出错了", MiuiToast.LENGTH_SHORT);
                        showConnectFail(true);
                    } else {
                        ExpressAdapter adapter = new ExpressAdapter(
                                getContext(),
                                status.list);

                        listView.setAdapter(adapter);
                        Log.d("express", "onPostExecute: type=" + status.type + " number=" + status.number + " status=" + status.deliverystatus);
                        showConnectFail(false);
                        //返回值是PickerView对应的 而不是CompanyName status.type为小写应转为大写
                        int ii = setPickerView(status.type.toUpperCase());
                        initPant(status, ii);

                    }
                }
            }.execute(type, number);
        }
    }

    /**
     * 状态
     *
     * @param w 解析到的状态编号
     * @return 状态
     */
    private String getExpressStats(int w) {
        //物流状态 1在途中 2派件中 3已签收 4派送失败(拒签等)
        String res;
        switch (w) {
            case 1:
                res = "在途中";
                break;
            case 2:
                res = "派件中";
                break;
            case 3:
                res = "已签收";
                break;
            default:
                res = "派件未完成";
                break;
        }
        return res;
    }

    /**
     * 显示物流状态、承运公司位置
     *
     * @param status 当前对应公司 null为自动识别
     * @param ii     companyType 对应角标
     */
    private void initPant(ExpressStatus status, int ii) {
        Animation anim1 = AnimationUtils.loadAnimation(getContext(), R.anim.express_tel);
        if (status == null) {
            express_company_stats.setText("——");
            express_company_name.setText("承运公司：" + "自动识别");
            express_company_type.setText("快递代号：" + "自动识别");
            express_company_tel.setText("116114");
        } else {
            express_company_stats.setText(getExpressStats(status.deliverystatus));
            express_company_name.setText("承运公司：" + companyNames.get(ii));
            express_company_type.setText("快递代号：" + companyTypes.get(ii));
            express_company_tel.setText(companyPhones.get(ii));
        }
        express_company_tel.startAnimation(anim1);
    }

    /**
     * 初始Tip View只需执行一次
     */
    private void initTipView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        viewI = inflater.inflate(R.layout.tip, null);
        RelativeLayout con = (RelativeLayout) viewI.findViewById(R.id.no_network);
        TextView textI = (TextView) viewI.findViewById(R.id.no_network_textView);
        ImageView imageI = (ImageView) viewI.findViewById(R.id.no_network_imageView);
        Button bu = (Button) viewI.findViewById(R.id.no_network_button);
        con.removeView(bu);
        imageI.setImageResource(R.mipmap.tip_express);
        textI.setText("输入订单号查询");
        frameLayout.addView(viewI);
    }

    /**
     * PickerView滚动事件监听
     * query按钮点击事件
     * express_pickerView_auto点击事件
     */
    private void initView() {

        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                int index = 0;
                if (text.equals("自动识别")) {
                    initPant(null, 0);
                    return;
                }
                for (int i = 0; i < companyNames.size(); i++) {
                    if (companyNames.get(i).equals(text)) {
                        index = i;
                        break;
                    }
                }
                Animation anim1 = AnimationUtils.loadAnimation(getContext(), R.anim.express_tel);
                express_company_name.setText("承运公司：" + companyNames.get(index));
                express_company_type.setText("快递代号：" + companyTypes.get(index));
                express_company_tel.setText(companyPhones.get(index));
                express_company_tel.startAnimation(anim1);

            }

        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeQuery();
            }
        });

        express_pickerView_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPant(null, 0);
            }
        });


        companyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = express_company_tel.getText().toString();
                final String[] ph = new String[1];
                final String[] strs = new CharUtils().spitExpressPhone(str);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("确认拨打官方电话");
               // builder.setMessage("确认拨打官方电话：");
                for (int i = 0; i < strs.length; i++)
                    Log.d("express", "onClick: "+strs[i].toString());

                builder.setSingleChoiceItems(strs, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ph[0] = strs[which];
                    }
                });

                builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+ph[0]));
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                builder.show();
            }
        });

    }

    /**
     * PickerView数据填充
     *
     * @param type 承运公司名称
     * @return type companyTypes对应的角标
     */
    private int setPickerView(String type) {
        int pos = DEFAULT_PICKER_SELECT;
        if (type == null) {
            pickerView.setData(pickerData, pos);
            return -1;
        } else {
            int po = DEFAULT_PICKER_SELECT;
            for (int i = 0; i < companyTypes.size(); i++) {
                if (companyTypes.get(i).equals(type.toUpperCase())) {
                    pos = i;
                    break;
                }
            }
            for (int i = 0; i < pickerData.size(); i++) {
                if (pickerData.get(i).equals(companyNames.get(pos))) {
                    po = i;
                    break;
                }
            }
            pickerView.setData(pickerData, po);
            return pos;
        }

    }

    /**
     * 查询结果显示位置未查询时用图片占位，查询失败显示“查询失败图片”
     *
     * @param bo true表示查询失败
     */
    private void showConnectFail(boolean bo) {

        if (bo) { // 需要显示（初始 || 出错）
            viewI.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_in);
            view.startAnimation(anim);
        }
        if (!bo) {
            viewI.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

    }


}
