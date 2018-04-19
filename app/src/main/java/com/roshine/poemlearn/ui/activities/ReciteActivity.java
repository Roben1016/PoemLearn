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
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.utils.LogUtil;
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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
    private SimpleRecyclertViewAdater<Poetry> mAdapter;
    private List<Poetry> listData = new ArrayList<>();
    private int page;
    private int limitCount = 10;//10条数据

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
        swipRecyclerView.setRefreshing(true);
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
        mAdapter = new SimpleRecyclertViewAdater<Poetry>(this,listData,R.layout.item_poem) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, Poetry itemBean, int position) {
                TextView tvTitle = holder.getView(R.id.tv_poem_title);
                TextView tvAuthor = holder.getView(R.id.tv_poem_author);
                TextView tvYear = holder.getView(R.id.tv_poem_year);
                TextView tvContent = holder.getView(R.id.tv_poem_content);
                if (itemBean != null) {
                    String correctPoem = itemBean.getP_content();
                    String correctPoem2 = correctPoem.replaceAll("[。、？.?]", "\r\n");
                    tvTitle.setText(itemBean.getP_name());
                    tvAuthor.setText(itemBean.getP_author());
                    tvYear.setText(itemBean.getP_source());
                    tvContent.setText(correctPoem2);

                }
            }
        };
        recyclertView.setAdapter(mAdapter);
        recyclertView.setOnItemClick(this);
    }

    private void initData(int page) {
        if(poemType == 1){
            BmobQuery<Poetry> query=new BmobQuery<Poetry>();
            query.addWhereEqualTo("p_type"," 宋词");
            query.setLimit(limitCount);
            if (page != 0) {
                query.setSkip(limitCount * page);
            }
            query.findObjects(new FindListener<Poetry>() {
                @Override
                public void done(List<Poetry> list, BmobException e) {
                    if(e ==null){
                         loadSuc(list,page);
                    }else{
                        loadFail(getResources().getString(R.string.load_failed),page);
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
            query.setLimit(limitCount);
            if (page != 0) {
                query.setSkip(limitCount * page);
            }
            query.findObjects(new FindListener<Poetry>() {
                @Override
                public void done(List<Poetry> list, BmobException e) {
                    if(e ==null){
                        loadSuc(list,page);
                    }else{
                        loadFail(getResources().getString(R.string.load_failed),page);
                        LogUtil.show("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    }
                }
            });
        }
    }

    private void loadFail(String string, int page) {
        toast(string);
        if(page == 0){
            swipRecyclerView.setRefreshing(false);
        }else{
            swipRecyclerView.setLoadMoreFinish(SwipeRecyclertView.LOAD_MORE_FAIL);
            this.page = page - 1;
        }
    }

    private void loadSuc(List<Poetry> list, int page) {
        swipRecyclerView.setRefreshing(false);
        if(list.size() == 0 || list.size() < limitCount){
            swipRecyclerView.setLoadMoreFinish(SwipeRecyclertView.LOAD_NO_MORE);
            toast(getResources().getString(R.string.no_more_text));
        }else{
            swipRecyclerView.setLoadMoreFinish(SwipeRecyclertView.LOAD_MORE_SUC);
        }
        if(this.page == 0){
            listData.clear();
        }
        listData.addAll(list);
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
        page = 0;
        initData(0);
    }

    @Override
    public void onLoadMore() {
        initData(++page);
    }

    @Override
    public void onReLoadMore() {
        initData(page);
    }

    @Override
    public void OnItemClick(int position, ViewHolder holder) {
        Bundle bundle = new Bundle();
        bundle.putString("p_id",listData.get(position).getObjectId());
        bundle.putInt("from",1);
        startActivity(PoetryDetailActivity.class, bundle);
    }
}
