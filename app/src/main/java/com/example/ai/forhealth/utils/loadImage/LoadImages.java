package com.example.ai.forhealth.utils.loadImage;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ai on 2016/12/20.
 * <p>
 * 通过AsyncTask方式异步加载图片<br>
 * 依赖于类{@link LoadBitmap#LoadBitmap()}<br>
 * 适用于任何listView和recycleVeiw加载图片，填入图片的控件必须有tag且tag为对应的URL<br>
 */

public class LoadImages {

    /**
     * 图片url地址集
     */
    private ArrayList<String> urls;

    /**
     * 保存要异步任务集
     */
    private Set<loadAsyncTask> mTask;

    /**
     * 联网加载图片保存于缓存中
     */
    private LoadBitmap loadBitmap;

    /**
     * 回调抽象类
     */
    private PostExecuteBitmap mPost;

    /**
     * 当图片正在下载时显示的图片
     */
    private Bitmap defaultBitmap;

    /**
     * 构造器
     *
     * @param defaultBitmap 联网下载所需图片失败时loadSingleImage方法将返回此图片
     * @param urls          图片的url地址集合
     * @see #loadSingleImage(String)
     */
    public LoadImages(Bitmap defaultBitmap, ArrayList<String> urls) {
        this.urls = urls;
        this.defaultBitmap = defaultBitmap;
        this.mTask = new HashSet<>();
        this.loadBitmap = new LoadBitmap();
    }

    /**
     * 添加加载图片任务到异步任务集中并执行
     *
     * @param pos pos
     */
    private void loadImage(int pos) {
        String url = urls.get(pos);
        loadAsyncTask task = new loadAsyncTask(url);
        task.execute();
        mTask.add(task);
    }

    /**
     * 必须调用初始化异步图片加载回调
     *
     * @param post 抽象类
     */
    public void initAsyncTaskCallback(PostExecuteBitmap post) {
        if (mPost == null)
            mPost = post;
    }

    /**
     * 获得图片
     * <p>1.如果图片已经存在于缓存中则直接返回图片</p>
     * <p>2.否则异步加载图片，图片加载完成后回调
     * {@link com.example.ai.forhealth.utils.loadImage.PostExecuteBitmap#postExecute}方法</p>
     *
     * @param url 图片对应的URL
     * @return 缓存中有图片返回图片，否则null
     * @throws Exception 调用此方法前未调用initAsyncTask方法初始化回调
     * @see #initAsyncTaskCallback(PostExecuteBitmap)
     */
    public Bitmap loadSingleImage(String url) throws Exception {
        if (mPost == null)
            throw new Exception("you have to call initAsyncTask method first!");
        Bitmap bitmap = loadBitmap.getBitmapFromCache(url);
        if (bitmap == null) {

            int pos = urls.indexOf(url);
            loadImage(pos);

            return null;
        } else {
            return bitmap;
        }
    }

    /**
     * 获得图片
     * <p>1.如果图片已经存在于缓存中则直接返回图片</p>
     * <p>2.否则异步加载图片，图片加载完成后回调
     * {@link com.example.ai.forhealth.utils.loadImage.PostExecuteBitmap#postExecute}方法</p>
     *
     * @param index 图片对应URL在构造时传入urls中的位置
     * @return 缓存中有图片返回图片，否则null
     * @throws Exception 调用此方法前未调用initAsyncTask方法初始化回调
     * @see #initAsyncTaskCallback(PostExecuteBitmap)
     */
    public Bitmap loadSingleImage(int index) throws Exception {
        if (mPost == null)
            throw new Exception("you have to call initAsyncTask method first!");
        String url = urls.get(index);
        Bitmap bitmap = loadBitmap.getBitmapFromCache(url);
        if (bitmap == null) {

            int pos = urls.indexOf(url);
            loadImage(pos);

            return defaultBitmap;
        } else {
            return bitmap;
        }
    }

    public void loadMultiImages(int start, int end) {
        for (int i = start; i < end; i++)
            loadImage(i);
    }

    /**
     * 取消所有加载图片的任务
     * 停止异步加载任务，防止卡顿
     */
    public void cancelAllTasks() {
        if (mTask != null) {
            for (loadAsyncTask task : mTask) {
                task.cancel(false);
            }
        }
    }

    /**
     * 继承AsyncTask实现异步加载
     */
    private class loadAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;

        loadAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            mPost.preExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = LoadBitmap.getBitmapFromUrl(url);
            if (bitmap != null) {
                loadBitmap.addBitmapToCache(url, bitmap);
            } else {
                return null;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null)
                mPost.postExecute(null, urls.indexOf(url), url);
            else
                mPost.postExecute(bitmap, urls.indexOf(url), url);
            mTask.remove(this);
        }
    }

}
