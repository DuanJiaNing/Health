package com.example.ai.forhealth.controller;

import com.example.ai.forhealth.model.health_knowledges.Query;

/**
 * Created by ai on 2016/11/26.
 */

public class Controller {

    private com.example.ai.forhealth.model.express.Query express;

    private com.example.ai.forhealth.model.health_knowledges.Query health;

    private com.example.ai.forhealth.model.juhe_news.Query news;

    private com.example.ai.forhealth.model.train_tickets.Query train;

    private com.example.ai.forhealth.model.weight.Query weight;


    private static final Controller instance = new Controller();
    private Controller(){}
    public static Controller getInstance()
    {
        return instance;
    }


    public Query getHealth() {
        if (health == null)
            health = Query.getInstance();
        return health;
    }
    public com.example.ai.forhealth.model.juhe_news.Query getNews() {
        if (news == null)
            news = com.example.ai.forhealth.model.juhe_news.Query.getInstance();
        return news;
    }

    public com.example.ai.forhealth.model.express.Query getExpress() {
        if (express == null)
            express = com.example.ai.forhealth.model.express.Query.getInstance();
        return express;
    }

    public com.example.ai.forhealth.model.train_tickets.Query getTrain() {
        if (train == null)
            train = com.example.ai.forhealth.model.train_tickets.Query.getInstance();
        return train;
    }

    public com.example.ai.forhealth.model.weight.Query getWeight() {
        if (weight == null)
            weight = com.example.ai.forhealth.model.weight.Query.getInstance();
        return weight;
    }
}
