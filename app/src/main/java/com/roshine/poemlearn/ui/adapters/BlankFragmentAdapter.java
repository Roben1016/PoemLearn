package com.roshine.poemlearn.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;

/**
 * @author L
 * @date 2017/8/28 13:50

 * @desc
 */
public class BlankFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    public BlankFragmentAdapter(FragmentManager fragmentManager, List<Fragment> mFragments) {
        super(fragmentManager);
        this.mFragments = mFragments;
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
