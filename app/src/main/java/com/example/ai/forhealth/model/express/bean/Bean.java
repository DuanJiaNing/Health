package com.example.ai.forhealth.model.express.bean;

/**
 * Created by ai on 2016/11/24.
 */

public class Bean<T> {
    public String status;
    public String msg;
    public T result;
}

//        "status": "0",
//        "msg": "ok",
//        "result": {
//        "list": [
//        {
//        "time": "2015-10-20 10:24:04",
//        "status": "顺丰速运 已收取快件"
//        },