package com.example.ai.forhealth.utils.rxjava;

import android.app.Activity;

/**
 *
 */

public abstract class RxJavaExecuteTasks<M> {

    private Activity mActivity;

    /**
     * ！！！！抽象方法不能用new创建！！！！
     * @param activity
     */
    protected RxJavaExecuteTasks(Activity activity)
    {
        this.mActivity = activity;
    }

    /**
     * 在非UI线程中执行数据获取
     * 取得后将结果传到UI线程中
     */
    void next() throws Exception {
        final M re = onExecute();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                oneTaskCompleted(re);
            }
        });
    }


    /**
     * !!!!!必须显示给予protected权限才能跨包创建匿名内部类!!!!!!
     * 单个（当前）异步任务具体对应要执行的操作
     * 抛出的异常若不处理则必须在onError方法中处理
     * @return
     */
    protected abstract M onExecute() throws Exception;


    /**
     * 单个异步任务结束
     * 在UI线程中执行
     */
    protected abstract void oneTaskCompleted(M t);

}
