package com.roshine.poemlearn.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BasePageFragment;
import com.roshine.poemlearn.beans.PoemBean;
import com.roshine.poemlearn.beans.PoemWordBean;
import com.roshine.poemlearn.utils.DisplayUtil;
import com.roshine.poemlearn.utils.StringUtils;
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
 * @date 2018/4/15 19:48
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class BlankFragement extends BasePageFragment {
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
    @BindView(R.id.tv_show_correct)
    TextView tvShowCorrect;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_reblank)
    TextView tvReBlank;

    private SimpleRecyclertViewAdater<PoemWordBean> mTopAdapter;
    private List<PoemWordBean> topData = new ArrayList<>();
    private List<PoemWordBean> bottomData = new ArrayList<>();
    private List<Integer> blankIndex = new LinkedList<>();
    private PoemBean mPoemBean;
    private SimpleRecyclertViewAdater<PoemWordBean> mBottomAdapter;
    private int currentLongClickPosition = -1;//当前长按的角标
    private int fragmentPosition;
    private int poemType;
    private boolean showCorrect = false;//是否显示正确答案
    private boolean firstPoint;
    private int maxTopCount;//最大显示字数
    private int topCount;//一行显示字数

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blank;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        int screenWidth = DisplayUtil.getScreenWidth(getActivity());
        maxTopCount = (screenWidth - DisplayUtil.dp2px(getActivity(), 10)) / (10 + DisplayUtil.dp2px(getActivity(), 30));
        Bundle arguments = getArguments();
        if (arguments != null) {
            fragmentPosition = arguments.getInt("position");
            poemType = arguments.getInt("poemType");
            mPoemBean = (PoemBean) arguments.getSerializable("poemBean");
            if (mPoemBean != null) {
                tvPoemTitle.setText(mPoemBean.getPoemTitle());
                tvPoemAuthor.setText(mPoemBean.getPoemAuthor());
                tvPoemYear.setText(mPoemBean.getPoemYear());
            }
        }
        initData();
        initTopRecycleView();
        initBottomRecycleView();
    }

    public static BlankFragement newInstance(int position,int poemType,PoemBean poemBean) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putInt("poemType",poemType);
        args.putSerializable("poemBean",poemBean);
        BlankFragement fragment = new BlankFragement();
        fragment.setArguments(args);
        return fragment;
    }


    private void initData() {
        blankIndex.clear();
        topData.clear();
        bottomData.clear();
        String correctPoem = mPoemBean.getCorrectPoem();
        correctPoem = correctPoem.replaceAll(" ","");
        for (int i = 0; i < correctPoem.length(); i++) {
            String word = String.valueOf(correctPoem.charAt(i));
            PoemWordBean poemWordBean = new PoemWordBean();
            poemWordBean.setBottomPosition(-1);
            poemWordBean.setCorrectWord(word);
            boolean chinesePunctuation = StringUtils.isChinesePunctuation(word.charAt(0));
            if (chinesePunctuation) {
                poemWordBean.setWord(word);
                if (!firstPoint) {
                    firstPoint = true;
                    topCount = i + 1;
                }
            } else {
                poemWordBean.setWord("");
                blankIndex.add(i);
            }
            poemWordBean.setPunctuation(chinesePunctuation);
            poemWordBean.setErrorWord(false);
            topData.add(poemWordBean);
        }
        String disturbPoem = StringUtils.getShuffleString(correctPoem);
        for (int i = 0; i < disturbPoem.length(); i++) {
            String word = String.valueOf(disturbPoem.charAt(i));
            boolean chinesePunctuation = StringUtils.isChinesePunctuation(word.charAt(0));
            if (!chinesePunctuation) {
                PoemWordBean poemWordBean = new PoemWordBean();
                poemWordBean.setBottomPosition(-1);
                poemWordBean.setWord(word);
                bottomData.add(poemWordBean);
            }
        }
        if (mTopAdapter != null) {
            mTopAdapter.notifyDataSetChanged();
        }
        if (mBottomAdapter != null) {
            mBottomAdapter.notifyDataSetChanged();
        }
    }

    private void initTopRecycleView() {
        if(topCount <= 0){
            topCount = maxTopCount;
        } else if(topCount >= maxTopCount){
            topCount = maxTopCount;
        }
        SpacesItemDecoration universalDecoration = new SpacesItemDecoration(5);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), topCount, GridLayoutManager.VERTICAL, false);
        rvTop.setLayoutManager(gridLayoutManager);
        mTopAdapter = new SimpleRecyclertViewAdater<PoemWordBean>(getActivity(), topData, R.layout.item_top_blank) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoemWordBean itemBean, int position) {
                TextView tvItem = holder.getView(R.id.tv_item_text);
                LinearLayout llContainer = holder.getView(R.id.ll_top_container);
                if (itemBean != null) {
                    if(showCorrect){
                        String word = itemBean.getCorrectWord();
                        tvItem.setText(word == null ? "" : word);
//                        tvItem.setBackground(null);
                        llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_bg));
                    }else{
                        String word = itemBean.getWord();
                        tvItem.setText(word == null ? "" : word);
                        if(itemBean.isErrorWord()){
                            llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_error_bg));
                        }else{
                            if (!StringUtils.isEmpty(word)) {
                                llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_bg));
                            } else {
                                llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bg));
                            }
                        }
                    }
                }
            }
        };
        rvTop.addItemDecoration(universalDecoration);
        rvTop.setAdapter(mTopAdapter);
        mTopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, ViewHolder holder) {
                if(showCorrect){
                    return;
                }
                PoemWordBean topPoemWordBean = topData.get(position);
                if (topPoemWordBean != null) {
                    String word = topPoemWordBean.getWord();
                    boolean punctuation = topPoemWordBean.isPunctuation();
                    int bottomPosition = topPoemWordBean.getBottomPosition();
                    if (currentLongClickPosition != -1) {
                        if(!punctuation){
                            if (StringUtils.isEmpty(word)) {
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
                        if (!StringUtils.isEmpty(word)) {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), topCount, GridLayoutManager.VERTICAL, false);
        rvBottom.setLayoutManager(gridLayoutManager);
        mBottomAdapter = new SimpleRecyclertViewAdater<PoemWordBean>(getActivity(), bottomData, R.layout.item_bottom_blank) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoemWordBean itemBean, int position) {
                TextView tvItem = holder.getView(R.id.tv_item_text);
                LinearLayout llContainer = holder.getView(R.id.ll_bottom_container);
                if (itemBean != null) {
                    String word = itemBean.getWord();
                    boolean longClick = itemBean.isLongClick();
                    tvItem.setText(word == null ? "" : word);
                    if (!StringUtils.isEmpty(word)) {
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
                if(showCorrect){
                    return;
                }
                if (currentLongClickPosition != -1) {
                    PoemWordBean poemWordBean = bottomData.get(currentLongClickPosition);
                    poemWordBean.setLongClick(false);
                    reflashBottomAdapter(currentLongClickPosition);
                    currentLongClickPosition = -1;
                } else {
                    PoemWordBean bottomPoemWordBean = bottomData.get(position);
                    if (!StringUtils.isEmpty(bottomPoemWordBean.getWord())) {
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
                if(showCorrect){
                    return false;
                }
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


    @Override
    public void onPageStart() {

    }

    @Override
    public void onPageEnd() {

    }

    @Override
    public void loadNetData() {
    }

    @OnClick(R.id.tv_show_correct)
    void showCorrect(){
        if(showCorrect){
            tvShowCorrect.setText(getActivity().getResources().getString(R.string.show_correct));
        }else{
            tvShowCorrect.setText(getActivity().getResources().getString(R.string.continue_text));

        }
        showCorrect = !showCorrect;
        if (mTopAdapter != null) {
            mTopAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.tv_reblank)
    void reBlank(){
        initData();
        currentLongClickPosition = -1;
        showCorrect = false;
        tvShowCorrect.setText(getActivity().getResources().getString(R.string.show_correct));
        if (mTopAdapter != null) {
            mTopAdapter.notifyDataSetChanged();
        }
        if (mBottomAdapter != null) {
            mBottomAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.tv_submit)
    void submit(){
        showCorrect = false;
        tvShowCorrect.setText(getActivity().getResources().getString(R.string.show_correct));
        if (mTopAdapter != null) {
            mTopAdapter.notifyDataSetChanged();
        }
        if (mBottomAdapter != null) {
            mBottomAdapter.notifyDataSetChanged();
        }
        boolean flag = true;
        for (int i = 0; i < topData.size(); i++) {
            PoemWordBean poemWordBean = topData.get(i);
            String word = poemWordBean.getWord();
            String correctWord = poemWordBean.getCorrectWord();
            if (correctWord != null && !correctWord.equals(word)) {
                flag = false;
                poemWordBean.setErrorWord(true);
                reflashTopAdapter(i);
            }
        }
        if(flag){
            toast(getActivity().getResources().getString(R.string.all_right));
        }
    }
}
