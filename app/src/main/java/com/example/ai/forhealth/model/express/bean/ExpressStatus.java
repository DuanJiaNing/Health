package com.example.ai.forhealth.model.express.bean;

import java.util.ArrayList;

/**
 *
 */
public class ExpressStatus {

    public String type;
    public String number;
    public int deliverystatus; //物流状态 1在途中 2派件中 3已签收 4派送失败(拒签等)
    public ArrayList<list> list;
}

//        issign	int	是否签收(已弃用，请使用deliverystatus)

//        time	string	时间
//        type	string	快递公司
//        number	string	快递单号
//        deliverystatus	int	物流状态 1在途中 2派件中 3已签收 4派送失败(拒签等)