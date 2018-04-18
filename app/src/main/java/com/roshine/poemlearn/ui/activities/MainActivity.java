package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseToolBarActivity {


    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.iv_tang)
    ImageView ivTang;
    @BindView(R.id.iv_song)
    ImageView ivSong;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.ll_search)
    void searchClick(){

    }
    @OnClick(R.id.iv_tang)
    void tangClick(){
        Bundle bundle = new Bundle();
        bundle.putInt("poemType",0);
        startActivity(GameMainActivity.class,bundle);
    }
    @OnClick(R.id.iv_song)
    void songClick(){
        Bundle bundle = new Bundle();
        bundle.putInt("poemType",1);
        startActivity(GameMainActivity.class,bundle);
    }

}
