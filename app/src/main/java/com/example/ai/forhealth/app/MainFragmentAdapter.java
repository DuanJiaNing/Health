package com.example.ai.forhealth.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ai on 2016/10/29.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private List<String> titlelist;

    private List<Fragment> fragmentsList;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragmentsList, List<String> title) {
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
