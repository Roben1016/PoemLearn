package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText(getResources().getString(R.string.history_text));
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
    void historyClick(){
        startActivity(HistoryActivity.class);
    }
}
