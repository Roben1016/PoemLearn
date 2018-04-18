package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.PoemBean;
import com.roshine.poemlearn.ui.adapters.BlankFragmentAdapter;
import com.roshine.poemlearn.ui.fragments.ConfirmGameFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Roshine
 * @date 2018/4/17 19:07
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class ConfirmGameActivity extends BaseToolBarActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tv_last_poem)
    TextView tvLastPoem;
    @BindView(R.id.tv_next_poem)
    TextView tvNextPoem;
    private int poemType;
    private int schoolType;
    private BlankFragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_confirmgame;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                poemType = extras.getInt("poemType");
                schoolType = extras.getInt("schoolType");
            }
        }
        ivBack.setVisibility(View.VISIBLE);
        if (1 == poemType) {
            tvTitle.setText(getResources().getString(R.string.beisong_song));
        } else {
            tvTitle.setText(getResources().getString(R.string.beisong_tang));
        }
        initData();
    }
    private void initData() {
        PoemBean poemBean = new PoemBean();
        poemBean.setCorrectPoem("折戟沉沙铁未销，自将磨洗认前朝。东风不与周郎便，铜雀春深锁二乔。");
        poemBean.setPoemAuthor("杜牧");
        poemBean.setPoemTitle("赤壁");
        poemBean.setPoemYear("唐");
        Fragment fragment = ConfirmGameFragment.newInstance(0, poemType, poemBean);
        fragmentList.add(fragment);

        PoemBean poemBean1 = new PoemBean();
        poemBean1.setCorrectPoem("雨暗初疑夜，风回忽报晴。淡云斜照著山明。细草软沙溪路、马蹄轻。 卯酒醒还困，仙材梦不成。蓝桥何处觅云英。只有多情流水、伴人行。");
        poemBean1.setPoemAuthor("柳宗元");
        poemBean1.setPoemTitle("江雪");
        poemBean1.setPoemYear("唐");
        Fragment fragment1 = ConfirmGameFragment.newInstance(1, poemType, poemBean1);
        fragmentList.add(fragment1);

        PoemBean poemBean2 = new PoemBean();
        poemBean2.setCorrectPoem("春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。");
        poemBean2.setPoemAuthor("孟浩然");
        poemBean2.setPoemTitle("春晓");
        poemBean2.setPoemYear("唐");
        Fragment fragment2 = ConfirmGameFragment.newInstance(2, poemType, poemBean2);
        fragmentList.add(fragment2);

        PoemBean poemBean3 = new PoemBean();
        poemBean3.setCorrectPoem("空山不见人，但闻人语响。返景入深林，复照青苔上。");
        poemBean3.setPoemAuthor("王维");
        poemBean3.setPoemTitle("鹿柴");
        poemBean3.setPoemYear("唐");
        Fragment fragment3 = ConfirmGameFragment.newInstance(3, poemType, poemBean3);
        fragmentList.add(fragment3);

        PoemBean poemBean4 = new PoemBean();
        poemBean4.setCorrectPoem("白日依山尽，黄河入海流。欲穷千里目，更上一层楼。");
        poemBean4.setPoemAuthor("王之涣");
        poemBean4.setPoemTitle("登鹳雀楼");
        poemBean4.setPoemYear("唐");
        Fragment fragment4 = ConfirmGameFragment.newInstance(4, poemType, poemBean4);
        fragmentList.add(fragment4);
        initViewPager();
    }

    private void initViewPager() {
        fragmentAdapter = new BlankFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(0 == position){
            tvLastPoem.setVisibility(View.GONE);
            tvNextPoem.setVisibility(View.VISIBLE);
        } else if(position == fragmentList.size() -1){
            tvNextPoem.setVisibility(View.GONE);
            tvLastPoem.setVisibility(View.VISIBLE);
        } else{
            tvNextPoem.setVisibility(View.VISIBLE);
            tvLastPoem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }
    @OnClick(R.id.tv_last_poem)
    void lastPoemClick(){
        int currentItem = mViewPager.getCurrentItem();
        toast(currentItem + "=点击上一页="+fragmentList.size());
        if(currentItem == 0){
            return;
        }
        mViewPager.setCurrentItem(currentItem -1);
    }
    @OnClick(R.id.tv_next_poem)
    void nextPoemClick(){
        int currentItem = mViewPager.getCurrentItem();
        toast(currentItem + "=点击下一页="+fragmentList.size());
        if(currentItem == fragmentList.size() -1){
            return;
        }
        mViewPager.setCurrentItem(currentItem + 1);
    }
}
