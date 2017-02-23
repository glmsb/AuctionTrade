package com.wyd.auctiontrade.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Description :Fragment与ViewPager配合使用时的适配器
 * Created by wyd on 2016/5/10.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> lists;

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> lists) {
        super(fm);
        this.lists = lists;
    }

    @Override
    public Fragment getItem(int position) {
        return lists.get(position);
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
