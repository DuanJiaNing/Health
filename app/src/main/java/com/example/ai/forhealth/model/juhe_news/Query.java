package com.example.ai.forhealth.model.juhe_news;

import com.example.ai.forhealth.model.juhe_news.bean.NewsData;
import com.example.ai.forhealth.model.juhe_news.bean.data;
import com.example.ai.forhealth.utils.Tools;

import java.util.ArrayList;

/**
 * 获得新闻数据
 *聚合数据API，免费的
 */
final public class Query {

    private final String URL = "http://v.juhe.cn/toutiao/index";

    private final String APPKEY = "6f9b759a1c2afec6be22014b64716e71";

    //单例设计模式“饿汉式”
    private static final Query query = new Query();
    private Query(){}
    public static Query getInstance()
    {
        return query;
    }

    /**
     * 获得所有频道
     * @return 数组
     */
    public static String[] getChannel()
    {
        return new String[] {
                "头条","社会","国内","国际","娱乐",
                "体育","军事","科技","财经","时尚"};
    }

    /**
     * 将中文转换为拼音
     * @param which 中文
     * @return 拼音
     */
    private String transfrom(String which)
    {
        String[] res = getChannel();
        String[] tr = new String[] {
                "top","shehui","guonei","guoji","yule",
                "tiyu","junshi","keji","caijing","shishang"};
        for (int i = 0; i < res.length; i++) {
            if (which.equals(res[i]))
                return tr[i];
        }
        return "top";
    }

    /**
     * 将URL对应获得的数据转化为需要的数据
     * @return 结果
     */
    public ArrayList<data> getNewsList(String which) throws Exception
    {
        String channel = transfrom(which);
        String url = URL+"?type="+channel+"&key="+APPKEY;
        NewsData data = Tools.getData(url,NewsData.class);

        return data.result.data;
    }
}