package com.example.ai.forhealth.utils.rxjava;

import android.app.Activity;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ai on 2016/11/26.
 */

abstract public class RxJavaTasksSubscribe {

    private Activity mActivity;

    private ArrayList<RxJavaExecuteTasks> tasks;

    private Subscriber<RxJavaExecuteTasks> subscriber;

    /**
     * 应传入存放获得数据的对象
     * ！！！！抽象方法不能用new创建！！！！
     */
    protected RxJavaTasksSubscribe(Activity a, ArrayList<RxJavaExecuteTasks> tasks)
    {
        this.mActivity = a;
        this.tasks = tasks;
    }



    /**
     * 只有在调用此方法后异步任务才会开始按序执行
     */
    public void execute()
    {
        onExecute();
    }

    /**
     * 异步任务集结束后执行
     * 在UI线程中运行
     */
   protected abstract void onCompleted();

    /**
     * 异步任务开始前执行
     * 在UI线程中运行
     */
    protected abstract void onStart();

    /**
     * 执行异步任务出错时执行
     * 在UI线程中运行
     * @param e throwable
     */
    protected abstract void onError(Throwable e);

    /**
     * 取消异步任务集（取消订阅）
     * 该方法会在onError()和onCompleted()后自动调用
     * 若你想中途停止异步任务，调用该方法
     */
    public void cancleSubscribe()
    {
        if (!subscriber.isUnsubscribed())
            subscriber.unsubscribe();
    }

    /**------------------------------------------------------------------------------*/
    private void finish()
    {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCompleted();
            }
        });
    }

    private void start()
    {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onStart();
            }
        });
    }

    private void error(final Throwable t)
    {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    onError(t);
            }
        });
    }

    private void onExecute()
    {
        //1.创建订阅者（它决定事件触发的时候将有怎样的行为）
        subscriber = new Subscriber<RxJavaExecuteTasks>() {

            @Override
            public void onStart() {
                start();
            }

            @Override
            public void onNext(RxJavaExecuteTasks s) {
                try {
                    s.next();
                } catch (Exception e) {
                    onError(e);
                }
            }

            //当不会再有新的onNext() 发出时，需要触发 onCompleted() 方法作为标志。
            @Override
            public void onCompleted() {
                finish();
                cancleSubscribe();
            }

            //事件队列异常。在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出。
            @Override
            public void onError(Throwable e) {
                error(e);
                cancleSubscribe();
            }};

        //2.Observable被观察者即被订阅者(它决定什么时候触发事件,以及触发怎样的事件)
        Observable observable = Observable.create(new Observable.OnSubscribe<RxJavaExecuteTasks>() {
            @Override
            public void call(Subscriber<? super RxJavaExecuteTasks> subscriber) {
                if (tasks != null)
                {
                    for (int i = 0; i < tasks.size(); i++) {
                        subscriber.onNext(tasks.get(i));
                    }
                    subscriber.onCompleted();
                }
                else
                    error(new Throwable("tasks is NULL"));
            }});

        //3.用 subscribe() 将订阅者和被订阅者联结起来，整条链子就开始（就可以）工作了。
        observable.subscribe(subscriber);

    }

}