package com.example.ai.forhealth.model.train_tickets;

import com.example.ai.forhealth.model.train_tickets.bean.Bean;
import com.example.ai.forhealth.model.train_tickets.bean.Outlets;
import com.example.ai.forhealth.model.train_tickets.bean.ResidueTicket;
import com.example.ai.forhealth.model.train_tickets.bean.StationList;
import com.example.ai.forhealth.model.train_tickets.bean.TicketPrice;
import com.example.ai.forhealth.utils.Tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *聚合数据12306火车票功能
 * 1.实时余票查询
 * 2.代售点查询
 * 3.站到站票价查询
 * 4.获得全国列车站点列表
 */

final public class Query {

    private final String AppKey = "f6a782fdddb214fa4ea3b5c78f2d9de7";

    //单例设计模式“饿汉式”
    private static final Query query = new Query();
    private Query(){}
    public static Query getInstance()
    {
        return query;
    }

    /**
     * 实时余票查询
     * @param from 出发站
     * @param to 终点站
     * @param date 日期
     * @param type 类型
     * @return 集合
     * @throws Exception API请求失败时抛出
     */
    public ArrayList<ResidueTicket> queryResidueTickets(String from, String to, long date, String type) throws Exception
    {
        //http://apis.juhe.cn/train/yp?key=申请的KEY&dtype=json&from=
        // %E4%B8%8A%E6%B5%B7%E8%99%B9%E6%A1%A5&to=%E6%B8%A9%E5%B7%9E&date=2014-06-28&tt=
        String da = new Date(date).toString();
        String url = "http://apis.juhe.cn/train/yp";
        String ur = url+"?key="+AppKey+"&dtype=json&from="+from+"&to="+to+"&date="+da+"&tt="+type;

        Bean<ArrayList<ResidueTicket>> bean = Tools.getData(ur, Bean.class);
        return bean.result;
    }

    /**
     * 代售点查询
     * @param procince 省份
     * @param city 城市
     * @param county 区/县
     * @return 集合
     * @throws Exception API请求失败时抛出
     */
    public ArrayList<Outlets> queryOutlets(String procince, String city, String county) throws Exception
    {
        String url = "http://apis.juhe.cn/train/dsd";
        //请求示例：http://apis.juhe.cn/train/dsd?key=您申请的KEY&dtype=json
        // &province=%E6%B5%99%E6%B1%9F&city=%E6%B8%A9%E5%B7%9E&county=%E4%B9%90%E6%B8%85%E5%B8%82
        String u = url+"?key="+AppKey+"&dtype=json"+"&province="+procince+"&city="+city+"&county="+county;

        Bean<ArrayList<Outlets>> data = Tools.getData(u,Bean.class);
        return data.result;
    }


    /**
     * 站到站票价查询
     * @param start 起点站
     * @param end 终点站
     * @return 集合
     * @throws Exception API请求失败时抛出
     */
    public List<TicketPrice> queryPriceBetweenStation(String start, String end) throws Exception
    {
        String url = "http://apis.juhe.cn/train/s2swithprice";
        //请求示例：http://apis.juhe.cn/train/s2swithprice
        // ?start=%E4%B8%8A%E6%B5%B7&end=%E8%8B%8F%E5%B7%9E&traintype=&key=
        String u = url+"?start="+start+"&end="+end+"traintype="+"&key="+AppKey;

        Bean<ArrayList<TicketPrice>> bean = Tools.getData(u,Bean.class);
        return bean.result;
    }

    /**
     * 获得全国列车站点列表
     * @return 集合
     * @throws Exception API请求失败时抛出
     */
    public List<StationList> getStationList() throws Exception
    {
        String url = "http://apis.juhe.cn/train/station.list.php";
        //请求示例：http://apis.juhe.cn/train/station.list.php?key=您申请的APPKEY
        String u = url+"?key="+AppKey;

        Bean<ArrayList<StationList>> bean = Tools.getData(u,Bean.class);
        return bean.result;
    }
}


