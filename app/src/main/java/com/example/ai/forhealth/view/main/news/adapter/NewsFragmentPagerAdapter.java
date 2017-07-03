package com.example.ai.forhealth.view.main.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ai on 2016/6/10.
 */
public class NewsFragmentPagerAdapter extends FragmentStatePagerAdapter {
//会把全部页面加载进来 不销毁
// 区别于FragmentStatePagerAdapter 按组管理动态销毁

    private List<String> titlelist;
    private List<Fragment> fragmentsList;

    public NewsFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList, List<String> title) {
        super(fm);
        this.fragmentsList = fragmentsList;
        this.titlelist = title;
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

    /**
     * 设置viewPager的标题
     * 返回标题
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }

}
