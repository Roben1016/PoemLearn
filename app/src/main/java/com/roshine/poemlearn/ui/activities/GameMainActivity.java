package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author L
 * @date 2018/4/14 23:11
 * @desc
 */
public class GameMainActivity extends BaseToolBarActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.iv_blank)
    ImageView ivBlank;
    @BindView(R.id.iv_recite)
    ImageView ivRecite;
    @BindView(R.id.iv_poem_game)
    ImageView ivPoemGame;
    @BindView(R.id.rb_primary)
    RadioButton rbPrimary;
    @BindView(R.id.rb_junior)
    RadioButton rbJunior;
    @BindView(R.id.rb_high)
    RadioButton rbHigh;
    @BindView(R.id.rg_games)
    RadioGroup rgGames;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_flying_orders)
    ImageView ivFlyingOrders;
    private int poemType;//0是唐诗  1是宋词
    private int schoolType;//0小学  1初中 2高中

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_main;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                poemType = bundle.getInt("poemType");
            }
        }
        setImages();
        rbPrimary.setChecked(true);
        if (poemType == 1) {//宋词没有初中高中之分
            rgGames.setVisibility(View.GONE);
        } else {
            rgGames.setVisibility(View.VISIBLE);
        }
        rgGames.setOnCheckedChangeListener(this);
        ivBack.setVisibility(View.VISIBLE);
    }

    private void setImages() {
        switch (poemType) {
            case 0:
                ivBlank.setImageDrawable(getResources().getDrawable(R.drawable.tang_blank));
                ivRecite.setImageDrawable(getResources().getDrawable(R.drawable.tang_recite));
                break;
            case 1:
                ivBlank.setImageDrawable(getResources().getDrawable(R.drawable.song_blank));
                ivRecite.setImageDrawable(getResources().getDrawable(R.drawable.song_recite));
                break;
            default:
                ivBlank.setImageDrawable(getResources().getDrawable(R.drawable.tang_blank));
                ivRecite.setImageDrawable(getResources().getDrawable(R.drawable.tang_recite));
                break;
        }
    }

    @OnClick(R.id.iv_blank)
    void blankClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType", poemType);
        bundle.putInt("schoolType", schoolType);
        startActivity(TianKongActivity.class, bundle);
    }

    @OnClick(R.id.iv_recite)
    void reciteClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType", poemType);
        bundle.putInt("schoolType", schoolType);
        startActivity(ReciteActivity.class, bundle);
    }

    @OnClick(R.id.iv_poem_game)
    void poemGameClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType", poemType);
        bundle.putInt("schoolType", schoolType);
        startActivity(BlankActivity.class, bundle);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_primary:
                schoolType = 0;
                break;
            case R.id.rb_junior:
                schoolType = 1;
                break;
            case R.id.rb_high:
                schoolType = 2;
                break;
            default:
                schoolType = 0;
                break;
        }
    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

    @OnClick(R.id.iv_flying_orders)
    void flyingOrderClick(){
        startActivity(FlyingOrderKeyWordActivity.class);
    }

}
