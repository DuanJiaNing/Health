package com.example.ai.forhealth.view.main.train.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ai on 2016/6/10.
 */
public class TrainFragmentPagerAdapter extends FragmentPagerAdapter {
//FragmentPagerAdapter 该类内的每一个生成的 Fragment 都将保存在内存之中 会把全部页面加载进来 不销毁
// FragmentStatePagerAdapter 按组管理动态销毁

    private List<Fragment> fragmentsList;

    public TrainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }
    /**
     * 返回页卡的数量
     */
    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

}
