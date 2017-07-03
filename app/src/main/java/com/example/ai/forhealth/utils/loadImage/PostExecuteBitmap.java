package com.example.ai.forhealth.utils.loadImage;

import android.graphics.Bitmap;

/**
 * Created by ai on 2016/12/20.
 */

public abstract class PostExecuteBitmap {
    /**
     * 异步联网获取图片结束后回调
     * @param bitmap 获得的图片
     * @param index 图片对应的url在集合中的位置
     * @param url 图片对应的url
     */
    protected abstract void postExecute(Bitmap bitmap,int index,String url);
    public void preExecute(){}

}
