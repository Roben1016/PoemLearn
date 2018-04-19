package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.ArraySet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.PoemBean;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.ui.adapters.BlankFragmentAdapter;
import com.roshine.poemlearn.ui.fragments.BlankFragement;
import com.roshine.poemlearn.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * @author Roshine
 * @date 2018/4/15 10:00
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class BlankActivity extends BaseToolBarActivity implements ViewPager.OnPageChangeListener {

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
            tvTitle.setText(getResources().getString(R.string.confirmgame_song));
        } else {
            tvTitle.setText(getResources().getString(R.string.confirmgame_tang));
        }
        initData();
    }

    private void initData() {
        showProgress();
        if(poemType == 1){
            BmobQuery<Poetry> query=new BmobQuery<Poetry>();
            query.addWhereEqualTo("p_type"," 宋词");
            //设置查询的SQL语句
            query.findObjects(new FindListener<Poetry>() {
                @Override
                public void done(List<Poetry> list, BmobException e) {
                    if(e ==null){
                        if(list!=null && list.size()>0){
                            loadSuc(list);
                        }else{
                            loadFail(getResources().getString(R.string.no_data));
                            LogUtil.show("smile", "查询成功，无数据返回");
                        }
                    }else{
                        loadFail(getResources().getString(R.string.load_failed));
                        LogUtil.show("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    }
                }
            });
        }else{
            String school = " 小学";

            if(schoolType == 1){//初中
                school = " 初中";
//            initJuniorPoem();
            }else if(schoolType == 2){
                school = " 高中";
//            initHighPoem();
            }else{
                school = " 小学";
//            initPrimaryPoem();
            }
            BmobQuery<Poetry> query=new BmobQuery<Poetry>();
            query.addWhereEqualTo("p_type",school);
            //设置查询的SQL语句
            query.findObjects(new FindListener<Poetry>() {
                @Override
                public void done(List<Poetry> list, BmobException e) {
                    if(e ==null){
                        if(list!=null && list.size()>0){
                            loadSuc(list);
                        }else{
                            loadFail(getResources().getString(R.string.no_data));
                            LogUtil.show("smile", "查询成功，无数据返回");
                        }
                    }else{
                        loadFail(getResources().getString(R.string.load_failed));
                        LogUtil.show("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    }
                }
            });
        }
    }

    private void loadSuc(List<Poetry> list) {
        hideProgress();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int i1 = random.nextInt(list.size());
            LogUtil.show("随机："+i1);
            Poetry poetry = list.get(i1);
            PoemBean poemBean = new PoemBean();
            poemBean.setCorrectPoem(poetry.getP_content());
            poemBean.setPoemAuthor(poetry.getP_author());
            poemBean.setPoemTitle(poetry.getP_name());
            poemBean.setPoemYear(poetry.getP_source());
            poemBean.setPoemId(poetry.getObjectId());
            Fragment fragment = BlankFragement.newInstance(i, poemType, poemBean);
            fragmentList.add(fragment);
        }
        fragmentAdapter = new BlankFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mViewPager.addOnPageChangeListener(this);
    }

    private void loadFail(String string) {
        hideProgress();
        toast(string);
        finish();
    }


    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }
    @OnClick(R.id.tv_last_poem)
    void lastPoemClick(){
        int currentItem = mViewPager.getCurrentItem();
        if(currentItem == 0){
            return;
        }
        mViewPager.setCurrentItem(currentItem -1);
    }
    @OnClick(R.id.tv_next_poem)
    void nextPoemClick(){
        int currentItem = mViewPager.getCurrentItem();
        if(currentItem == fragmentList.size() -1){
            return;
        }
        mViewPager.setCurrentItem(currentItem + 1);
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
