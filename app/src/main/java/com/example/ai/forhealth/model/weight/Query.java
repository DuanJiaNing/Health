package com.example.ai.forhealth.model.weight;

import com.example.ai.forhealth.model.weight.bean.Bean;
import com.example.ai.forhealth.model.weight.bean.result;
import com.example.ai.forhealth.utils.Tools;

/**
 * 标准体重计算器
 */
final public class Query {

    private final String URL = "http://api.jisuapi.com/weight/bmi";

    private final String appKey = "5171de6ae9fd57a6";

    //单例设计模式“饿汉式”
    private static final Query query = new Query();
    private Query(){}
    public static Query getInstance()
    {
        return query;
    }

    /**
     * 获得标准体重计算结果
     * @param sex 性别  male female
     * @param height 身高 CM
     * @param weight 体重 KG
     * @return 结果
     * @throws Exception 计算失败
     */
    public result getData(String sex, final String height, String weight) throws Exception
    {
        //请求示例：http://api.jisuapi.com/weight/bmi?appkey=yourappkey&sex=男&height=172&weight=60
        result re = new result();
        String url = URL+"?appkey="+appKey+"&sex="+sex+"&height="+height+"&weight="+weight;

        Bean bean = Tools.getData(url, Bean.class);

        return bean.result;
    }

}
