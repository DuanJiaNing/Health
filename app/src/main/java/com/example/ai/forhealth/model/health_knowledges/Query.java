package com.example.ai.forhealth.model.health_knowledges;

import com.example.ai.forhealth.model.health_knowledges.Bean.Bean;
import com.example.ai.forhealth.model.health_knowledges.Bean.Category;
import com.example.ai.forhealth.model.health_knowledges.Bean.ListDetailInformation;
import com.example.ai.forhealth.model.health_knowledges.Bean.ListInformation;
import com.example.ai.forhealth.model.health_knowledges.Bean.data;
import com.example.ai.forhealth.utils.Tools;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

final public class Query {

//    private final String Appkey = "b9990fb4eff0848ebccafe7facde1517";
    private final String Appkey = "f9fd0edc95e04c2d3dd8e54b3249b0b8";

    //单例设计模式“饿汉式”
    private static final Query query = new Query();
    private Query(){}
    public static Query getInstance()
    {
        return query;
    }

    /**
     * 获得健康知识的一级分类列表
     * @return 集合
     * @throws Exception 获取数据失败
     */
    public ArrayList<Category> queryCategoryList() throws Exception
    {
        List<Category> categoryBeanList = new ArrayList<Category>();
        String url = "http://japi.juhe.cn/health_knowledge/categoryList";
        //请求示例：http://japi.juhe.cn/health_knowledge/categoryList?key=您申请的KEY
        String u = url+"?key="+Appkey;

        Bean<ArrayList<Category>> bean = Tools.getData(u, new TypeToken<Bean<ArrayList<Category>>>(){}.getType());
        return bean.result;
    }

    /**
     * 获得健康知识的信息列表（二级列表）
     * @param page 请求页码（20条/页）
     * @param id 知识分类的id 可由queryCategoryList获得
     * @return 集合
     * @throws Exception 获取数据失败
     */
    public ArrayList<data> queryListInformation(int page, String id) throws Exception
    {
        //请求一页，默认20条/页，id为null时为请求全部
        List<data> informationBeanList = new ArrayList<data>();
        String url = "http://japi.juhe.cn/health_knowledge/infoList";
        //请求示例：http://japi.juhe.cn/health_knowledge/infoList?key=您申请的KEY
        //http://japi.juhe.cn/health_knowledge/infoList?key=b9990fb4eff0848ebccafe7facde1517&page=3&limit=20&id=21

        String u = url+"?key="+Appkey+"&page="+page+"&limit=20"+"&id="+id;

        Bean<ListInformation> bean = Tools.getData(u,new TypeToken<Bean<ListInformation>>(){}.getType());
        return bean.result.data;
    }

    /**
     * 根据健康知识ID获得详情
     * @param id 健康知识id 可由queryListInformation获得
     * @return 集合
     * @throws Exception 获取数据失败
     */
    public ListDetailInformation queryListDetailInformation(String id) throws Exception
    {
        ListDetailInformation listDetailInformation ;
        String url = "http://japi.juhe.cn/health_knowledge/infoDetail";
        //请求示例：http://japi.juhe.cn/health_knowledge/infoDetail?id=18207&key=您申请的KEY
        String u = url+"?id="+id+"&key="+Appkey;

        listDetailInformation = Tools.getData(u,ListDetailInformation.class);
        return listDetailInformation;
    }

    /**
     * 最新健康知识,通过当前最新的ID，取得最新的知识列表。通过该方法可以做到数据的不重复。但该接口也并非一定要用！
     * @param id 当前最新的知识的id 可为null
     * @param categoryId 分类ID，取得该分类下的最新数据 可为null
     * @param num 返回最新关键词的条数，默认rows=20 可为null
     * @return 集合
     * @throws Exception 获取数据失败
     */
    public ArrayList<data> queryInformationDetail(String id,String categoryId,int num) throws Exception
    {
        int nu = !(num > 0) ? 21:num;

        String url = "http://japi.juhe.cn/health_knowledge/infoNews";
        //请求示例：http://japi.juhe.cn/health_knowledge/infoNews?key=KEY&id=21
        String u = url+"?key="+Appkey+"&classify="+categoryId+"&rows="+nu+"&id="+id;

        Bean<ListInformation> bean = Tools.getData(u,new TypeToken<Bean<ListInformation>>(){}.getType());
        return bean.result.data;
    }
}
