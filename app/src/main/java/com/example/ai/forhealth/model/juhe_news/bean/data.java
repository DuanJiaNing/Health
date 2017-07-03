package com.example.ai.forhealth.model.juhe_news.bean;

/**
 *
 */
public class data {

    public String title; // "title": 标题
    public String date; // "date":时间
    public String author_name; //"author_name":作者
    public String thumbnail_pic_s; //"thumbnail_pic_s":小图
    public String thumbnail_pic_s02; //"thumbnail_pic_s02":中图,只有头条才有
    public String thumbnail_pic_s03; //"thumbnail_pic_s03":大图
    public String url;//"url": 新闻链接
    public String uniquekey;/*唯一标识*/
    public String type;//"type":类型一
    public String realtype;//"realtype":类型二

    @Override
    public String toString() {
        return "data{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", author_name='" + author_name + '\'' +
                ", thumbnail_pic_s='" + thumbnail_pic_s + '\'' +
                ", thumbnail_pic_s02='" + thumbnail_pic_s02 + '\'' +
                ", thumbnail_pic_s03='" + thumbnail_pic_s03 + '\'' +
                ", url='" + url + '\'' +
                ", uniquekey='" + uniquekey + '\'' +
                ", type='" + type + '\'' +
                ", realtype='" + realtype + '\'' +
                '}';
    }
}













//
//{
//    "reason": "成功的返回",
//    "result": {
//        "stat": "1",
//        "data": [
//            {
//            "title": "巫山云雨枉断肠：女摄影师Erika Lust记录的性爱",/*标题*/
//            "date": "2016-06-13 10:31",/*时间*/
//            "author_name": "POCO摄影",/*作者*/
//            "thumbnail_pic_s": "http://09.imgmini.eastday.com/mobile/20160613/20160613103108_7b015493398e7fd13dda3a5c
//                                      e315b1c8_1_mwpm_03200403.jpeg",/*图片1*/
//            "thumbnail_pic_s02": "http://09.imgmini.eastday.com/mobile/20160613/20160613103108_7b015493398e7fd13dda3a5ce315b1c8_1_mwpl_05500201.jpeg",/*图片2*/
//            "thumbnail_pic_s03": "http://09.imgmini.eastday.com/mobile/20160613/20160613103108_7b015493398e7fd13dda3a5ce315b1c8_1_mwpl_05500201.jpeg",/*图片3*/
//            "url": "http://mini.eastday.com/mobile/160613103108379.html?qid=juheshuju",/*新闻链接*/
//            "uniquekey": "160613103108379",/*唯一标识*/
//            "type": "头条",/*类型一*/
//            "realtype": "娱乐"/*类型二*/
//        },
//        ...]}}
