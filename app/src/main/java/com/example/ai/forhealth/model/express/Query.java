package com.example.ai.forhealth.model.express;

import com.example.ai.forhealth.model.express.bean.Bean;
import com.example.ai.forhealth.model.express.bean.CompanyNo;
import com.example.ai.forhealth.model.express.bean.ExpressStatus;
import com.example.ai.forhealth.utils.Tools;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * 快递查询
 *极速数据API，1000免费后收费
 */
public class Query {

    private final String URL = "http://api.jisuapi.com/express/query";

    private final String APPKEY = "5171de6ae9fd57a6";

    //单例设计模式“饿汉式”
    private static final Query instance = new Query();
    private Query(){}
    public static Query getInstance()
    {
        return instance;
    }

    /**
     * 获得清单
     * @param type 快递公司编号
     * @param number 单号
     * @return 结果
     */
    public ExpressStatus getDetail(String type, String number) throws Exception
    {
        //请求示例：http://api.jisuapi.com/express/query?appkey=yourappkey&type=sfexpress&number=931658943036
        String url = URL+"?appkey="+APPKEY+"&type="+type+"&number="+number;

//        Bean<ExpressStatus> bean = Tools.getData(url, Bean.class);
        Bean<ExpressStatus> bean = Tools.getData(url, new TypeToken<Bean<ExpressStatus>>(){}.getType());
        return bean.result;
    }

    /**
     * 获得快递公司名和对应的信息
     * @return list
     */
    public com.example.ai.forhealth.model.express.bean.Bean<ArrayList<CompanyNo>> getList() throws Exception
    {
        //http://api.jisuapi.com/express/type?appkey=yourappkey
        String url = "http://api.jisuapi.com/express/type?appkey="+APPKEY;

        Bean<ArrayList<CompanyNo>> bean = Tools.getData(url, Bean.class);
        return bean;
    }

}
