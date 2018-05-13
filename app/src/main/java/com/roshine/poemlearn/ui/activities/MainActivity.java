package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.utils.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseToolBarActivity {


    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.iv_tang)
    ImageView ivTang;
    @BindView(R.id.iv_song)
    ImageView ivSong;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_left)
    TextView tvLeft;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText(getResources().getString(R.string.history_text));
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(getResources().getString(R.string.exchange_user));
    }

    @OnClick(R.id.ll_search)
    void searchClick() {
        startActivity(SearchActivity.class);
    }

    @OnClick(R.id.iv_tang)
    void tangClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType", 0);
        startActivity(GameMainActivity.class, bundle);
    }

    @OnClick(R.id.iv_song)
    void songClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType", 1);
        startActivity(GameMainActivity.class, bundle);
    }

    @OnClick(R.id.tv_title_right)
    void historyClick() {
        startActivity(HistoryActivity.class);
    }

    //点击切换账号
    @OnClick(R.id.tv_left)
    void exchangeUser(){
        //切换账号时，把之前保存的账号和密码清空
        SPUtil.setParam(this,"username","");
        SPUtil.setParam(this,"password", "");
        SPUtil.setParam(this,"checed",false);
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
