package com.example.ai.forhealth.utils.loadImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 联网加载图片保存于缓存中<br>
 * 1.初始化时创建缓存空间（缓存空间大小为maxMemory/4）保存下载的图片<br>
 * 2.调用getBitmapFromCache时为取到图片，则应调用getBitmapFromUrl通过URL下载图片，若之后还用到此图片则应调
 *  用addBitmapToCache方法将图片保存到同所需该类对象下。<br>
 * 3.缓存中图片保存形式为（Object key,Bitmap image）建议使用图片对应的URL作为key
 */
public class LoadBitmap {

    /**
     * 一个对象值能有一个缓存
     */
    private LruCache<String,Bitmap> mCaches;

    /**
     * 构造器，构造出缓存空间
     */
    public LoadBitmap()
    {
        int cacheSize = ( (int) Runtime.getRuntime().maxMemory() )/4;
        mCaches = new LruCache<String,Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 把图片加入缓存
     * @param url 图片对应的URL作为标识图片的名字
     * @param bitmap 图片
     */
    public void addBitmapToCache(String url,Bitmap bitmap)
    {
        if (getBitmapFromCache(url) == null)
            mCaches.put(url,bitmap);
    }

    /**
     * 从缓存中获得图片
     * @param url URL（图片名）
     * @return 有返回图片无返回null
     */
    public Bitmap getBitmapFromCache(String url)
    {
        return (Bitmap) mCaches.get(url);
    }

    /**
     * 联网通过URL下载图片
     * @param urlString URL
     * @return 图片
     */
    public static Bitmap getBitmapFromUrl(String urlString) {

        InputStream is ;
        try {
            is = new URL(urlString).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return BitmapFactory.decodeStream(is);

    }


}
