package com.example.ai.forhealth.model.train_tickets.bean;

import java.util.ArrayList;

/**
 * 站到站票价查询
 */

public class TicketPrice {

    public String train_no;
    public String train_type;
    public String start_station;
    public String start_station_type;
    public String end_station;
    public String end_station_type;
    public String start_time;
    public String end_time;
    public String run_time;
    public String run_distance;
    public ArrayList<price_list> type_price;

}
//{
//        "reason": "查询成功",
//        "result": {
//        "list": [
//        {
    //        "train_no": "K526",
    //        "train_type": "快速",
    //        "start_station": "上海南",
    //        "start_station_type": "过",
    //        "end_station": "苏州",
    //        "end_station_type": "过",
    //        "start_time": "04:19",
    //        "end_time": "05:43",
    //        "run_time": "1小时24分钟",
    //        "run_distance": "98公里",
    //        "price_list": [
    //        {
    //        "price_type": "硬座",
    //        "price": "15.5"
    //        },
    //        {
    //        "price_type": "硬卧",
    //        "price": "66.5"
    //        }
    //        ]
//        },
//        {
    //        "train_no": "K528",
    //        "train_type": "快速",
    //        "start_station": "上海南",
    //        "start_station_type": "过",
    //        "end_station": "苏州",
    //        "end_station_type": "过",
    //        "start_time": "04:29",
    //        "end_time": "05:35",
    //        "run_time": "1小时6分钟",
    //        "run_distance": "98公里",
    //        "price_list": [
    //        {
    //        "price_type": "硬座",
    //        "price": "15.5"
    //        },
    //        {
    //        "price_type": "软卧",
    //        "price": "98.5"
    //        }
    //        ]
//        },