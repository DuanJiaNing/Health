package com.example.ai.forhealth.model.train_tickets.bean;

/**
 * Created by ai on 2016/11/24.
 */

public class Bean<T> {
    public String resultcode;
    public String reason;
    public T result;
    public String error_code;

}
//        {
//        "resultcode": "200",
//        "reason": "Successed!",
//        "result": {  },
//        "error_code": 0
//        }