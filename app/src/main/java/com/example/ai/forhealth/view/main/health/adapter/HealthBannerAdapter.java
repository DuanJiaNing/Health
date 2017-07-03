package com.example.ai.forhealth.view.main.health.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ai.forhealth.model.health_knowledges.Bean.data;
import com.example.ai.forhealth.utils.BitmapUtil;
import com.example.ai.forhealth.utils.loadImage.LoadBitmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ai on 2016/12/4.
 */

public class HealthBannerAdapter extends PagerAdapter {


    private ArrayList<data> datas;

    private ArrayList<View> viewList;

    public HealthBannerAdapter(ArrayList<data> datas) {
        this.datas = datas;

        initView();
    }

    private void initView() {

        for (int i = 0; i < datas.size(); i++) {

        }

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(viewList.get(position));

        return viewList.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        container.removeView(viewList.get(position));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    class LoadImages {

        private LoadBitmap loadBitmap;

        private String[] tags;

        private Set<loadAsyncTask> mTask;

        private Bitmap defaultBitmap;


        public LoadImages(ListView listView, String[] tags, Bitmap bitmap) {
            this.mTask = new HashSet<>();
            loadBitmap = new LoadBitmap();
            this.tags = tags;
            this.defaultBitmap = bitmap;
        }

        public void loadMultiImages(int num) {
            for (int i = 0; i < num; i++) {
                String url = tags[i];
                Bitmap bitmap = loadBitmap.getBitmapFromCache(url);
                if (bitmap == null) {
                    loadAsyncTask task = new loadAsyncTask(url);
                    task.execute();
                    mTask.add(task);
                } else {
                    /**---------------*/

                }
            }
        }


        private class loadAsyncTask extends AsyncTask<Void, Void, Bitmap> {
            private String url;

            public loadAsyncTask(String url) {
                this.url = url;
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap = LoadBitmap.getBitmapFromUrl(url);
                if (bitmap != null) {
                    loadBitmap.addBitmapToCache(url, BitmapUtil.toRoundBitmap(bitmap));
                } else {
                    bitmap = defaultBitmap;
                    loadBitmap.addBitmapToCache(url, bitmap);
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
//                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
//                if (imageView != null && bitmap != null) {
//                    imageView.setImageBitmap(BitmapUtil.toRoundBitmap(bitmap));
//
//                    AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
//                    animation.setDuration(600);
//                    imageView.startAnimation(animation);
//
//                }
//                mTask.remove(this);
            }
        }
    }


}
