package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.MvpBaseActivity;
import com.roshine.poemlearn.beans.PoemBean;
import com.roshine.poemlearn.ui.adapters.BlankFragmentAdapter;
import com.roshine.poemlearn.ui.contracts.BlankContract;
import com.roshine.poemlearn.ui.fragments.BlankFragement;
import com.roshine.poemlearn.ui.presenters.BlankPresenter;
import com.roshine.poemlearn.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Roshine
 * @date 2018/4/15 10:00
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class BlankActivity extends MvpBaseActivity<BlankContract.IBlankView, BlankPresenter> implements BlankContract.IBlankView, ViewPager.OnPageChangeListener {

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
    private List<Fragment> fragmentList = new ArrayList<>();
    private BlankFragmentAdapter fragmentAdapter;
    private int schoolType;

    @Override
    public BlankPresenter getPresenter() {
        return new BlankPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blank;
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
            tvTitle.setText(getResources().getString(R.string.song_blank_text));
        } else {
            tvTitle.setText(getResources().getString(R.string.tang_blank_text));
        }
        initData();
    }

    private void initData() {
        if(schoolType == 1){//初中
            initJuniorPoem();
        }else if(schoolType == 2){
            initHighPoem();
        }else{
            initPrimaryPoem();
        }

        fragmentAdapter = new BlankFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mViewPager.addOnPageChangeListener(this);

    }

    private void initHighPoem() {
        PoemBean poemBean = new PoemBean();
        poemBean.setCorrectPoem("折戟沉沙铁未销，自将磨洗认前朝。东风不与周郎便，铜雀春深锁二乔。");
        poemBean.setPoemAuthor("杜牧");
        poemBean.setPoemTitle("赤壁");
        poemBean.setPoemYear("唐");
        Fragment fragment = BlankFragement.newInstance(0, poemType, poemBean);
        fragmentList.add(fragment);

        PoemBean poemBean1 = new PoemBean();
        poemBean1.setCorrectPoem("千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。");
        poemBean1.setPoemAuthor("柳宗元");
        poemBean1.setPoemTitle("江雪");
        poemBean1.setPoemYear("唐");
        Fragment fragment1 = BlankFragement.newInstance(1, poemType, poemBean1);
        fragmentList.add(fragment1);

        PoemBean poemBean2 = new PoemBean();
        poemBean2.setCorrectPoem("春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。");
        poemBean2.setPoemAuthor("孟浩然");
        poemBean2.setPoemTitle("春晓");
        poemBean2.setPoemYear("唐");
        Fragment fragment2 = BlankFragement.newInstance(2, poemType, poemBean2);
        fragmentList.add(fragment2);

        PoemBean poemBean3 = new PoemBean();
        poemBean3.setCorrectPoem("空山不见人，但闻人语响。返景入深林，复照青苔上。");
        poemBean3.setPoemAuthor("王维");
        poemBean3.setPoemTitle("鹿柴");
        poemBean3.setPoemYear("唐");
        Fragment fragment3 = BlankFragement.newInstance(3, poemType, poemBean3);
        fragmentList.add(fragment3);

        PoemBean poemBean4 = new PoemBean();
        poemBean4.setCorrectPoem("白日依山尽，黄河入海流。欲穷千里目，更上一层楼。");
        poemBean4.setPoemAuthor("王之涣");
        poemBean4.setPoemTitle("登鹳雀楼");
        poemBean4.setPoemYear("唐");
        Fragment fragment4 = BlankFragement.newInstance(4, poemType, poemBean4);
        fragmentList.add(fragment4);
    }

    private void initJuniorPoem() {
        PoemBean poemBean = new PoemBean();
        poemBean.setCorrectPoem("折戟沉沙铁未销，自将磨洗认前朝。东风不与周郎便，铜雀春深锁二乔。");
        poemBean.setPoemAuthor("杜牧");
        poemBean.setPoemTitle("赤壁");
        poemBean.setPoemYear("唐");
        Fragment fragment = BlankFragement.newInstance(0, poemType, poemBean);
        fragmentList.add(fragment);

        PoemBean poemBean1 = new PoemBean();
        poemBean1.setCorrectPoem("千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。");
        poemBean1.setPoemAuthor("柳宗元");
        poemBean1.setPoemTitle("江雪");
        poemBean1.setPoemYear("唐");
        Fragment fragment1 = BlankFragement.newInstance(1, poemType, poemBean1);
        fragmentList.add(fragment1);

        PoemBean poemBean2 = new PoemBean();
        poemBean2.setCorrectPoem("春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。");
        poemBean2.setPoemAuthor("孟浩然");
        poemBean2.setPoemTitle("春晓");
        poemBean2.setPoemYear("唐");
        Fragment fragment2 = BlankFragement.newInstance(2, poemType, poemBean2);
        fragmentList.add(fragment2);

        PoemBean poemBean3 = new PoemBean();
        poemBean3.setCorrectPoem("空山不见人，但闻人语响。返景入深林，复照青苔上。");
        poemBean3.setPoemAuthor("王维");
        poemBean3.setPoemTitle("鹿柴");
        poemBean3.setPoemYear("唐");
        Fragment fragment3 = BlankFragement.newInstance(3, poemType, poemBean3);
        fragmentList.add(fragment3);

        PoemBean poemBean4 = new PoemBean();
        poemBean4.setCorrectPoem("白日依山尽，黄河入海流。欲穷千里目，更上一层楼。");
        poemBean4.setPoemAuthor("王之涣");
        poemBean4.setPoemTitle("登鹳雀楼");
        poemBean4.setPoemYear("唐");
        Fragment fragment4 = BlankFragement.newInstance(4, poemType, poemBean4);
        fragmentList.add(fragment4);
    }

    private void initPrimaryPoem() {
        PoemBean poemBean = new PoemBean();
        poemBean.setCorrectPoem("床前明月光，疑是地上霜。举头望明月，低头思故乡。");
        poemBean.setPoemAuthor("李白");
        poemBean.setPoemTitle("静夜思");
        poemBean.setPoemYear("唐");
        Fragment fragment = BlankFragement.newInstance(0, poemType, poemBean);
        fragmentList.add(fragment);

        PoemBean poemBean1 = new PoemBean();
        poemBean1.setCorrectPoem("千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。");
        poemBean1.setPoemAuthor("柳宗元");
        poemBean1.setPoemTitle("江雪");
        poemBean1.setPoemYear("唐");
        Fragment fragment1 = BlankFragement.newInstance(1, poemType, poemBean1);
        fragmentList.add(fragment1);

        PoemBean poemBean2 = new PoemBean();
        poemBean2.setCorrectPoem("春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。");
        poemBean2.setPoemAuthor("孟浩然");
        poemBean2.setPoemTitle("春晓");
        poemBean2.setPoemYear("唐");
        Fragment fragment2 = BlankFragement.newInstance(2, poemType, poemBean2);
        fragmentList.add(fragment2);

        PoemBean poemBean3 = new PoemBean();
        poemBean3.setCorrectPoem("空山不见人，但闻人语响。返景入深林，复照青苔上。");
        poemBean3.setPoemAuthor("王维");
        poemBean3.setPoemTitle("鹿柴");
        poemBean3.setPoemYear("唐");
        Fragment fragment3 = BlankFragement.newInstance(3, poemType, poemBean3);
        fragmentList.add(fragment3);

        PoemBean poemBean4 = new PoemBean();
        poemBean4.setCorrectPoem("白日依山尽，黄河入海流。欲穷千里目，更上一层楼。");
        poemBean4.setPoemAuthor("王之涣");
        poemBean4.setPoemTitle("登鹳雀楼");
        poemBean4.setPoemYear("唐");
        Fragment fragment4 = BlankFragement.newInstance(4, poemType, poemBean4);
        fragmentList.add(fragment4);
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


    @Nullable
    @Override
    public void loadSuccess(Object datas) {

    }

    @Override
    public void loadFail(String message) {

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
}
