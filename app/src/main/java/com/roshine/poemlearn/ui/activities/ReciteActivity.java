package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.PoemBean;
import com.roshine.poemlearn.widgets.recyclerview.base.SimpleRecyclertViewAdater;
import com.roshine.poemlearn.widgets.recyclerview.base.ViewHolder;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemClickListener;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnLoadMoreListener;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnRefreshListener;
import com.roshine.poemlearn.widgets.recyclerview.refreshandload.LSAutoRecyclertView;
import com.roshine.poemlearn.widgets.recyclerview.refreshandload.SwipeRecyclertView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Roshine
 * @date 2018/4/17 20:27
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class ReciteActivity extends BaseToolBarActivity implements OnRefreshListener, OnLoadMoreListener, OnItemClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.swip_recycler_view)
    SwipeRecyclertView swipRecyclerView;
    private int poemType;
    private int schoolType;
    private LSAutoRecyclertView recyclertView;
    private SimpleRecyclertViewAdater<PoemBean> mAdapter;
    private List<PoemBean> listData = new ArrayList<>();
    private int page;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recite;
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
            tvTitle.setText(getResources().getString(R.string.recite_song));
        } else {
            tvTitle.setText(getResources().getString(R.string.recite_tang));
        }
        initRecycleView();
        initData(0);
    }

    private void initRecycleView() {
        swipRecyclerView.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipRecyclerView.setLoadMoreProgressBarDrawbale(getResources().getDrawable(R.drawable.pb_theme_colorprimary));
        swipRecyclerView.setOnRefreshListener(this);
        swipRecyclerView.setOnloadMoreListener(this);
        recyclertView = swipRecyclerView.getRecyclertView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleRecyclertViewAdater<PoemBean>(this,listData,R.layout.item_poem) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoemBean itemBean, int position) {
                TextView tvTitle = holder.getView(R.id.tv_poem_title);
                TextView tvAuthor = holder.getView(R.id.tv_poem_author);
                TextView tvYear = holder.getView(R.id.tv_poem_year);
                TextView tvContent = holder.getView(R.id.tv_poem_content);
                if (itemBean != null) {
                    String correctPoem = itemBean.getCorrectPoem();
                    String correctPoem2 = correctPoem.replaceAll("[。、？.?]", "\r\n");
                    tvTitle.setText(itemBean.getPoemTitle());
                    tvAuthor.setText(itemBean.getPoemAuthor());
                    tvYear.setText(itemBean.getPoemYear());
                    tvContent.setText(correctPoem2);

                }
            }
        };
        recyclertView.setAdapter(mAdapter);
        recyclertView.setOnItemClick(this);
    }

    private void initData(int page) {
        if(page == 0 && listData != null){
            listData.clear();
        }
        PoemBean poemBean = new PoemBean();
        poemBean.setCorrectPoem("折戟沉沙铁未销，自将磨洗认前朝。东风不与周郎便，铜雀春深锁二乔。");
        poemBean.setPoemAuthor("杜牧");
        poemBean.setPoemTitle("赤壁");
        poemBean.setPoemYear("唐");
        listData.add(poemBean);

        PoemBean poemBean1 = new PoemBean();
        poemBean1.setCorrectPoem("千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。");
        poemBean1.setPoemAuthor("柳宗元");
        poemBean1.setPoemTitle("江雪");
        poemBean1.setPoemYear("唐");
        listData.add(poemBean1);

        PoemBean poemBean2 = new PoemBean();
        poemBean2.setCorrectPoem("春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。");
        poemBean2.setPoemAuthor("孟浩然");
        poemBean2.setPoemTitle("春晓");
        poemBean2.setPoemYear("唐");
        listData.add(poemBean2);

        PoemBean poemBean3 = new PoemBean();
        poemBean3.setCorrectPoem("空山不见人，但闻人语响。返景入深林，复照青苔上。");
        poemBean3.setPoemAuthor("王维");
        poemBean3.setPoemTitle("鹿柴");
        poemBean3.setPoemYear("唐");
        listData.add(poemBean3);

        PoemBean poemBean4 = new PoemBean();
        poemBean4.setCorrectPoem("白日依山尽，黄河入海流。欲穷千里目，更上一层楼。");
        poemBean4.setPoemAuthor("王之涣");
        poemBean4.setPoemTitle("登鹳雀楼");
        poemBean4.setPoemYear("唐");
        listData.add(poemBean4);
        swipRecyclerView.setRefreshing(false);
        if(page == 10){
            swipRecyclerView.setLoadMoreFinish(SwipeRecyclertView.LOAD_NO_MORE);
        }else{
            swipRecyclerView.setLoadMoreFinish(SwipeRecyclertView.LOAD_MORE_SUC);
        }
        reflashAdapter();
    }

    private void reflashAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

    @Override
    public void onRefresh() {
        initData(0);
    }

    @Override
    public void onLoadMore() {
        initData(page++);
    }

    @Override
    public void onReLoadMore() {

    }

    @Override
    public void OnItemClick(int position, ViewHolder holder) {

    }
}
