package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.MvpBaseActivity;
import com.roshine.poemlearn.beans.PoemBean;
import com.roshine.poemlearn.beans.PoemWordBean;
import com.roshine.poemlearn.ui.contracts.BlankContract;
import com.roshine.poemlearn.ui.presenters.BlankPresenter;
import com.roshine.poemlearn.utils.AppStringUtils;
import com.roshine.poemlearn.utils.LogUtil;
import com.roshine.poemlearn.widgets.recyclerview.base.SimpleRecyclertViewAdater;
import com.roshine.poemlearn.widgets.recyclerview.base.ViewHolder;
import com.roshine.poemlearn.widgets.recyclerview.decoration.SpacesItemDecoration;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemClickListener;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
public class BlankActivity extends MvpBaseActivity<BlankContract.IBlankView, BlankPresenter> implements BlankContract.IBlankView {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_poem_title)
    TextView tvPoemTitle;
    @BindView(R.id.rv_top)
    RecyclerView rvTop;
    @BindView(R.id.rv_bottom)
    RecyclerView rvBottom;
    @BindView(R.id.tv_poem_author)
    TextView tvPoemAuthor;
    @BindView(R.id.tv_poem_year)
    TextView tvPoemYear;
    private int poemType;
    private SimpleRecyclertViewAdater<PoemWordBean> mTopAdapter;
    private List<PoemWordBean> topData = new ArrayList<>();
    private List<PoemWordBean> bottomData = new ArrayList<>();
    private List<Integer> blankIndex = new LinkedList<>();
    private PoemBean mPoemBean;
    private SimpleRecyclertViewAdater<PoemWordBean> mBottomAdapter;
    private int currentLongClickPosition = -1;//当前长按的角标

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
            }
        }
        ivBack.setVisibility(View.VISIBLE);
        if (1 == poemType) {
            tvTitle.setText(getResources().getString(R.string.song_blank_text));
        } else {
            tvTitle.setText(getResources().getString(R.string.tang_blank_text));
        }
        initData();
        initTopRecycleView();
        initBottomRecycleView();
    }

    private void initData() {
        mPoemBean = new PoemBean();
        mPoemBean.setCorrectPoem("床前明月光，疑是地上霜。举头望明月，低头思故乡。");
        mPoemBean.setDisturbPoem("疑是地上，霜床前明。月光低头，思故乡举头望明月。");
        mPoemBean.setPoemAuthor("李白");
        mPoemBean.setPoemTitle("静夜思");
        mPoemBean.setPoemYear("唐");
        String correctPoem = mPoemBean.getCorrectPoem();
        String[] split = correctPoem.split("");
        for (int i = 0; i < split.length; i++) {
            if (0 != i) {
                String word = split[i];
                PoemWordBean poemWordBean = new PoemWordBean();
                poemWordBean.setBottomPosition(-1);
                boolean chinesePunctuation = AppStringUtils.isChinesePunctuation(word.charAt(0));
                if (chinesePunctuation) {
                    poemWordBean.setWord(word);
                } else {
                    poemWordBean.setWord("");
                    blankIndex.add(i - 1);
                }
                poemWordBean.setPunctuation(chinesePunctuation);
                topData.add(poemWordBean);
            }
        }

        String disturbPoem = mPoemBean.getDisturbPoem();
        String[] split2 = disturbPoem.split("");
        for (int i = 0; i < split2.length; i++) {
            if (0 != i) {
                String word = split2[i];
                boolean chinesePunctuation = AppStringUtils.isChinesePunctuation(word.charAt(0));
                if (!chinesePunctuation) {
                    PoemWordBean poemWordBean = new PoemWordBean();
                    poemWordBean.setBottomPosition(-1);
                    poemWordBean.setWord(word);
                    bottomData.add(poemWordBean);
                    LogUtil.show(i + ":" + word);
                }
            }
        }
        LogUtil.show("topSize:" + topData.size() + "==bottomSize:" + bottomData.size());
        tvPoemTitle.setText(mPoemBean.getPoemTitle());
        tvPoemAuthor.setText(mPoemBean.getPoemAuthor());
        tvPoemYear.setText(mPoemBean.getPoemYear());
    }

    private void initTopRecycleView() {
        SpacesItemDecoration universalDecoration = new SpacesItemDecoration(5);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        rvTop.setLayoutManager(gridLayoutManager);
        mTopAdapter = new SimpleRecyclertViewAdater<PoemWordBean>(this, topData, R.layout.item_top_blank) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoemWordBean itemBean, int position) {
                TextView tvItem = holder.getView(R.id.tv_item_text);
                LinearLayout llContainer = holder.getView(R.id.ll_top_container);
                if (itemBean != null) {
                    String word = itemBean.getWord();
                    tvItem.setText(word == null ? "" : word);
                    if (!AppStringUtils.isEmpty(word)) {
                        llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_bg));
                    } else {
                        llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bg));
                    }
                }
            }
        };
        rvTop.addItemDecoration(universalDecoration);
        rvTop.setAdapter(mTopAdapter);
        mTopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, ViewHolder holder) {
                PoemWordBean topPoemWordBean = topData.get(position);
                if (topPoemWordBean != null) {
                    String word = topPoemWordBean.getWord();
                    boolean punctuation = topPoemWordBean.isPunctuation();
                    int bottomPosition = topPoemWordBean.getBottomPosition();
                    if (currentLongClickPosition != -1) {
                        if(!punctuation){
                            if (AppStringUtils.isEmpty(word)) {
                                blankIndex.remove(Integer.valueOf(position));
                            } else {
                                PoemWordBean poemWordBean = bottomData.get(bottomPosition);
                                poemWordBean.setWord(word);
                                reflashBottomAdapter(bottomPosition);
                            }
                            PoemWordBean poemWordBean = bottomData.get(currentLongClickPosition);
//                            PoemWordBean topBean = topData.get(position);
                            String word1 = poemWordBean.getWord();
                            poemWordBean.setLongClick(false);
                            poemWordBean.setWord("");
                            reflashBottomAdapter(currentLongClickPosition);

                            topPoemWordBean.setWord(word1);
                            topPoemWordBean.setBottomPosition(currentLongClickPosition);
                            reflashTopAdapter(position);
                            currentLongClickPosition = -1;
                        }
                    } else {
                        if (!AppStringUtils.isEmpty(word)) {
                            if (!punctuation) {
                                if (bottomPosition != -1 && bottomPosition < bottomData.size()) {
                                    PoemWordBean bottomPoemBean = bottomData.get(bottomPosition);
                                    bottomPoemBean.setWord(word);
                                    reflashBottomAdapter(bottomPosition);

                                    topPoemWordBean.setWord("");
                                    reflashTopAdapter(position);
                                    blankIndex.add(position);
                                    Collections.sort(blankIndex);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void initBottomRecycleView() {
        SpacesItemDecoration universalDecoration = new SpacesItemDecoration(5);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        rvBottom.setLayoutManager(gridLayoutManager);
        mBottomAdapter = new SimpleRecyclertViewAdater<PoemWordBean>(this, bottomData, R.layout.item_bottom_blank) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoemWordBean itemBean, int position) {
                TextView tvItem = holder.getView(R.id.tv_item_text);
                LinearLayout llContainer = holder.getView(R.id.ll_bottom_container);
                if (itemBean != null) {
                    String word = itemBean.getWord();
                    boolean longClick = itemBean.isLongClick();
                    tvItem.setText(word == null ? "" : word);
                    if (!AppStringUtils.isEmpty(word)) {
                        if (longClick) {
                            llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_long_bg));
                        } else {
                            llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_bg));
                        }
                    } else {
                        llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bg));
                    }
                }
            }
        };
        rvBottom.addItemDecoration(universalDecoration);
        rvBottom.setAdapter(mBottomAdapter);
        mBottomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, ViewHolder holder) {
                toast("单击："+position);
                if (currentLongClickPosition != -1) {
                    PoemWordBean poemWordBean = bottomData.get(currentLongClickPosition);
                    poemWordBean.setLongClick(false);
                    reflashBottomAdapter(currentLongClickPosition);
                    currentLongClickPosition = -1;
                } else {
                    PoemWordBean bottomPoemWordBean = bottomData.get(position);
                    if (!AppStringUtils.isEmpty(bottomPoemWordBean.getWord())) {
                        PoemWordBean topPoemWordBean = topData.get(blankIndex.get(0));
                        topPoemWordBean.setWord(bottomPoemWordBean.getWord());
                        topPoemWordBean.setBottomPosition(position);
                        bottomPoemWordBean.setWord("");
                        reflashTopAdapter(blankIndex.get(0));
                        reflashBottomAdapter(position);
                        blankIndex.remove(0);
                    }
                }
            }
        });
        mBottomAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position, ViewHolder holder) {
                if (currentLongClickPosition == -1) {
                    PoemWordBean bottomPoemBean = bottomData.get(position);
                    bottomPoemBean.setLongClick(true);
                    reflashBottomAdapter(position);
                    currentLongClickPosition = position;
                } else {
                    PoemWordBean poemWordBean = bottomData.get(currentLongClickPosition);
                    poemWordBean.setLongClick(false);
                    reflashBottomAdapter(currentLongClickPosition);

                    PoemWordBean bottomPoemBean = bottomData.get(position);
                    bottomPoemBean.setLongClick(true);
                    reflashBottomAdapter(position);
                    currentLongClickPosition = position;
                }
                return false;
            }
        });
    }

    private void reflashBottomAdapter(int position) {
        if (mBottomAdapter != null) {
            mBottomAdapter.notifyItemChanged(position);
        }
    }

    private void reflashTopAdapter(int position) {
        if (mTopAdapter != null) {
            mTopAdapter.notifyItemChanged(position);
        }
    }

    @Nullable
    @Override
    public void loadSuccess(Object datas) {

    }

    @Override
    public void loadFail(String message) {

    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

}
