package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.Config;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.beans.PoetryHistory;
import com.roshine.poemlearn.widgets.recyclerview.base.SimpleRecyclertViewAdater;
import com.roshine.poemlearn.widgets.recyclerview.base.ViewHolder;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author Roshine
 * @date 2018/4/19 21:15
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc 学习历史
 */
public class HistoryActivity extends BaseToolBarActivity implements OnItemClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private SimpleRecyclertViewAdater<PoetryHistory> mAdapter;
    private List<PoetryHistory> listData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.learn_history));
        initRecycleView();
        initData();
    }

    private void initData() {
        showProgress();
        BmobQuery<PoetryHistory> query=new BmobQuery<>();
        query.addWhereEqualTo("u_id", Config.getInstance().user.getObjectId());
        query.order("-updatedAt");
        query.findObjects(new FindListener<PoetryHistory>() {
            @Override
            public void done(List<PoetryHistory> list, BmobException e) {
                if(e == null){
                    if (list != null && list.size() > 0) {
                        loadSuc(list);
                    }else{
                        loadFailed(getResources().getString(R.string.no_data));
                    }
                }else{
                    loadFailed(getResources().getString(R.string.load_failed));
                }
            }
        });
    }

    private void loadFailed(String string) {
        hideProgress();
        toast(string);
    }

    private void loadSuc(List<PoetryHistory> list) {
        hideProgress();
        listData.clear();
        listData.addAll(list);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleRecyclertViewAdater<PoetryHistory>(this, listData, R.layout.item_history) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoetryHistory itemBean, int position) {
                TextView tvTitle = holder.getView(R.id.tv_title);
                TextView tvAuthor = holder.getView(R.id.tv_author);
                TextView tvDate = holder.getView(R.id.tv_date);
                tvTitle.setText(itemBean.getP_title());
                tvAuthor.setText(itemBean.getP_author());
                tvDate.setText(itemBean.getUpdatedAt());
            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    public void OnItemClick(int position, ViewHolder holder) {
        Bundle bundle = new Bundle();
        bundle.putString("p_id",listData.get(position).getP_id());
        bundle.putInt("from",0);
        startActivity(PoetryDetailActivity.class, bundle);
    }
    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }
}
