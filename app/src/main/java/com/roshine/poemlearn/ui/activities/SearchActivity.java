package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.widgets.recyclerview.base.SimpleRecyclertViewAdater;
import com.roshine.poemlearn.widgets.recyclerview.base.ViewHolder;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author Roshine
 * @date 2018/4/19 22:34
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class SearchActivity extends BaseToolBarActivity implements SearchView.OnQueryTextListener, OnItemClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private SimpleRecyclertViewAdater<Poetry> mAdapter;
    private List<Poetry> listData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        searchview.setOnQueryTextListener(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleRecyclertViewAdater<Poetry>(this, listData, R.layout.item_history) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, Poetry itemBean, int position) {
                TextView tvTitle = holder.getView(R.id.tv_title);
                TextView tvAuthor = holder.getView(R.id.tv_author);
                TextView tvDate = holder.getView(R.id.tv_date);
                tvTitle.setText(itemBean.getP_name());
                tvAuthor.setText(itemBean.getP_author());
                tvDate.setVisibility(View.GONE);
            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        query(query);
        return false;
    }

    private void reflashAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        query(newText);
        return false;
    }

    private void query(String newText) {
        BmobQuery<Poetry> eq1 = new BmobQuery<Poetry>();
        eq1.addWhereContains("p_name", newText);
        BmobQuery<Poetry> eq2 = new BmobQuery<Poetry>();
        eq2.addWhereContains("p_author", newText);
//        BmobQuery<Poetry> eq3 = new BmobQuery<Poetry>();//暂时不差内容
//        eq3.addWhereContains("p_content", newText);
        List<BmobQuery<Poetry>> queries = new ArrayList<BmobQuery<Poetry>>();
        queries.add(eq1);
        queries.add(eq2);
//        queries.add(eq3);
        BmobQuery<Poetry> mainQuery = new BmobQuery<Poetry>();
        mainQuery.or(queries);
        mainQuery.findObjects(new FindListener<Poetry>() {
            @Override
            public void done(List<Poetry> list, BmobException e) {
                if(e == null){
                    listData.clear();
                    if (list != null && list.size() > 0) {
                        toast("搜索成功："+list.size());
                        listData.addAll(list);
                    }
                    reflashAdapter();
                }else{
                    listData.clear();
                    toast("搜索失败");
                    reflashAdapter();
                }
            }
        });
    }

    @Override
    public void OnItemClick(int position, ViewHolder holder) {
        Bundle bundle = new Bundle();
        bundle.putString("p_id",listData.get(position).getObjectId());
        bundle.putInt("from",2);
        startActivity(PoetryDetailActivity.class, bundle);
    }

    @OnClick(R.id.tv_title_right)
    void searchClick(){
        query(searchview.getQuery().toString());
    }
    @OnClick(R.id.iv_back)
    void backClick(){
        finish();
    }
}
