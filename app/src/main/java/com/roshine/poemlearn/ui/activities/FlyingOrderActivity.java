package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.FlyiingOrderBean;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.utils.LogUtil;
import com.roshine.poemlearn.widgets.recyclerview.base.SimpleRecyclertViewAdater;
import com.roshine.poemlearn.widgets.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author L
 * @desc 飞花令游戏界面
 */
public class FlyingOrderActivity extends BaseToolBarActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.et_input_poetry)
    EditText etInputPoetry;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    private String keyword;//关键字

    private List<Poetry> queryData = new ArrayList<>();
    private boolean isSuccess;//是否加载成功
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private View decorView;
    private List<FlyiingOrderBean> listData = new ArrayList<>();
    private List<String> inputList = new ArrayList<>();//输入过的文本
    private SimpleRecyclertViewAdater<FlyiingOrderBean> mAdapter;
    private int currentStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flying_order;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //主要为了设置软键盘弹出只顶起输入框
        decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        globalLayoutListener = getGlobalLayoutListener(decorView, contentView);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                keyword = bundle.getString("keyword");
            }
        }
        tvTitle.setText(keyword);
        ivBack.setVisibility(View.VISIBLE);
        query(keyword);
        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvContent.setLayoutManager(linearLayoutManager);
        mAdapter = new SimpleRecyclertViewAdater<FlyiingOrderBean>(this, listData, R.layout.item_flying_order) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, FlyiingOrderBean itemBean, int position) {
                RelativeLayout rlRound = holder.getView(R.id.rl_round);
                TextView tvRound = holder.getView(R.id.tv_round);
                TextView tvStatus = holder.getView(R.id.tv_status);
                TextView tvContent = holder.getView(R.id.tv_content);
                TextView tvInfo = holder.getView(R.id.tv_info);
                TextView tvVS = holder.getView(R.id.tv_vs);
                View line = holder.getView(R.id.view_line);
                int userType = itemBean.getUserType();
                int roundCount = itemBean.getRoundCount();
                int status = itemBean.getStatus();
                String content = itemBean.getContent();
                Poetry poetry = itemBean.getPoetry();
                rlRound.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);
                tvInfo.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                tvVS.setVisibility(View.GONE);
                LogUtil.show("状态："+status);
                if(0 == userType){//如果是用户
                    tvVS.setVisibility(View.VISIBLE);
                    rlRound.setVisibility(View.VISIBLE);
                    tvRound.setText(String.format(getResources().getString(R.string.round_text),roundCount));
                    //TODO 设置状态
                    if(poetry != null){
                        tvInfo.setVisibility(View.VISIBLE);
                        tvInfo.setText("—— "+poetry.getP_source()+" · "+poetry.getP_author()+"《"+poetry.getP_name()+"》");
                    } else {
                        switch (status) {
                            case 4:
                                tvInfo.setVisibility(View.VISIBLE);
                                tvInfo.setText(getResources().getString(R.string.null_poetry));
                                break;
                            case 5:
                                tvInfo.setVisibility(View.VISIBLE);
                                tvInfo.setText(getResources().getString(R.string.null_poetry));
                                break;

                            default:
                        	    break;
                        }
                    }

                    switch (status) {
                        case 1:
                            tvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setBackground(getResources().getDrawable(R.drawable.currect_bg));
                            tvStatus.setTextColor(getResources().getColor(R.color.green));
                    	    tvStatus.setText(getResources().getString(R.string.flying_order_success));
                    	    break;
                        case 2:
                            tvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setBackground(getResources().getDrawable(R.drawable.error_bg));
                            tvStatus.setTextColor(getResources().getColor(R.color.red));
                            tvStatus.setText(getResources().getString(R.string.flying_order_success_half));
                            break;
                        case 3:
                            tvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setBackground(getResources().getDrawable(R.drawable.error_bg));
                            tvStatus.setTextColor(getResources().getColor(R.color.red));
                            tvStatus.setText(getResources().getString(R.string.flying_order_no_match));
                            break;
                        case 4:
                            tvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setBackground(getResources().getDrawable(R.drawable.error_bg));
                            tvStatus.setTextColor(getResources().getColor(R.color.red));
                            tvStatus.setText(getResources().getString(R.string.flying_order_no_poetry));
                            break;
                        case 5:
                            tvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setBackground(getResources().getDrawable(R.drawable.error_bg));
                            tvStatus.setTextColor(getResources().getColor(R.color.red));
                            tvStatus.setText(getResources().getString(R.string.flying_order_reinput));
                            break;
                        default:
                    	    break;
                    }
                    tvContent.setText(content);

                } else {//如果是机器匹配
                    line.setVisibility(View.VISIBLE);
                    String p_content = poetry.getP_content();
                    rlRound.setVisibility(View.GONE);
                    ////先将.?!等标点替换成-, 再分解成诗句数组,数组里有,号
                    String replace = p_content.replaceAll("[。？！!.?]", "-");
                    String[] split = replace.split("-");
                    String poetryContent = p_content;
                    for (int i = 0; i < split.length; i++) {
                        if(split[i].contains(keyword)){
                            poetryContent = split[i].replaceAll("[，,、]","\r\n");
                            tvContent.setText(poetryContent);
                            break;
                        }
                    }
                    tvInfo.setVisibility(View.VISIBLE);
                    tvInfo.setText("—— "+poetry.getP_source()+" · "+poetry.getP_author()+"《"+poetry.getP_name()+"》");
                }

            }
        };
        rvContent.setAdapter(mAdapter);
    }

    private void query(String keyword) {
        showProgress(getResources().getString(R.string.quering_text));
        BmobQuery<Poetry> eq = new BmobQuery<>();
        eq.addWhereContains("p_content", keyword);
        List<BmobQuery<Poetry>> queries = new ArrayList<>();
        queries.add(eq);
        BmobQuery<Poetry> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.findObjects(new FindListener<Poetry>() {
            @Override
            public void done(List<Poetry> list, BmobException e) {
                hideProgress();
                if(e == null){
                    isSuccess = true;
                    queryData.clear();
                    if (list != null && list.size() > 0) {
                        queryData.addAll(list);
                    }
                    LogUtil.show("查询数量："+queryData.size());
                }else{
                    isSuccess = false;
                    queryData.clear();
                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

    @OnClick(R.id.btn_submit)
    void submitClick() {
        if(TextUtils.isEmpty(etInputPoetry.getText())){
            toast(getResources().getString(R.string.input_null));
            return;
        }
        String inputText = etInputPoetry.getText().toString();
        FlyiingOrderBean bean = new FlyiingOrderBean();
        bean.setUserType(0);
        bean.setRoundCount((listData.size() / 2 ) + 1);
        String content = inputText ;
        currentStatus = 0;
        for (int i = 0; i < inputList.size(); i++) {
            String text = inputList.get(i);
            if (text.equals(inputText)) {
                currentStatus = 5;
                bean.setPoetry(null);
                bean.setContent(content);
                bean.setStatus(currentStatus);
                reflashAdapter(listData.size());
                return;
            }
        }
        inputList.add(inputText);

        //在查询到的诗词集合中再进行搜索
        if(inputText.contains("，") || inputText.contains(",")){//包含逗号
            String replace = inputText.replaceAll("[，,]", "-");
            replace = replace.replaceAll("[.。！!?？]","");
            String[] split = replace.split("-");
            if(split.length>1){
                String firstLine = split[0];
                String secondLine = split[1];
                for (int i = 0; i < queryData.size(); i++) {
                    Poetry poetry = queryData.get(i);
                    String p_content = poetry.getP_content();
                    if(p_content.contains(firstLine)){
                        if(p_content.contains(secondLine)){
                            LogUtil.show("查到该句诗了");
                            content = firstLine + "\r\n" +secondLine;
                            bean.setPoetry(poetry);
                            currentStatus = 1;
                            bean.setStatus(currentStatus);
                            queryData.remove(i);//匹配一条后移除该数据
                            break;
                        }
                    }
                }
                bean.setContent(content);
                if(currentStatus != 1){//如果未匹配到，则查询数据库，看是否存在数据
                    LogUtil.show("状态不为1，去数据库查询");
                    BmobQuery<Poetry> eq = new BmobQuery<>();
                    eq.addWhereContains("p_content", firstLine);
                    List<BmobQuery<Poetry>> queries = new ArrayList<>();
                    queries.add(eq);
                    BmobQuery<Poetry> mainQuery = new BmobQuery<>();
                    mainQuery.or(queries);
                    mainQuery.findObjects(new FindListener<Poetry>() {
                        @Override
                        public void done(List<Poetry> list, BmobException e) {
                            if(e == null){
                                if (list != null && list.size() > 0) {
                                    Poetry poetry = list.get(0);
                                    if(poetry.getP_content().contains(secondLine)){
                                        currentStatus = 3;
                                        bean.setStatus(currentStatus);
                                        bean.setPoetry(poetry);
                                        listData.add(bean);
                                        reflashAdapter(listData.size());
                                    } else {
                                        currentStatus = 4;
                                        bean.setStatus(currentStatus);
                                        bean.setPoetry(null);
                                        listData.add(bean);
                                        reflashAdapter(listData.size());
                                    }
                                } else{
                                    currentStatus = 4;
                                    bean.setStatus(currentStatus);
                                    bean.setPoetry(null);
                                    listData.add(bean);
                                    reflashAdapter(listData.size());
                                }
                            }else{
                                currentStatus = 4;
                                bean.setStatus(currentStatus);
                                bean.setPoetry(null);
                                listData.add(bean);
                                reflashAdapter(listData.size());
                            }
                        }
                    });
                }else{
                    LogUtil.show("状态为1，刷新列表");
                    LogUtil.show(bean.getPoetry()==null?"为空":"不为空");
                    bean.setStatus(currentStatus);
                    listData.add(bean);
                    reflashAdapter(listData.size());
                }
            } else {
                BmobQuery<Poetry> eq = new BmobQuery<>();
                eq.addWhereContains("p_content", inputText);
                List<BmobQuery<Poetry>> queries = new ArrayList<>();
                queries.add(eq);
                BmobQuery<Poetry> mainQuery = new BmobQuery<>();
                mainQuery.or(queries);
                mainQuery.findObjects(new FindListener<Poetry>() {
                    @Override
                    public void done(List<Poetry> list, BmobException e) {
                        if(e == null){
                            if (list != null && list.size() > 0) {
                                Poetry poetry = list.get(0);
                                currentStatus = 3;
                                bean.setStatus(currentStatus);
                                bean.setPoetry(poetry);
                                listData.add(bean);
                                reflashAdapter(listData.size());
                            }else{
                                currentStatus = 4;
                                bean.setStatus(currentStatus);
                                bean.setPoetry(null);
                                listData.add(bean);
                                reflashAdapter(listData.size());
                            }
                        }else{
                            currentStatus = 4;
                            bean.setStatus(currentStatus);
                            bean.setPoetry(null);
                            listData.add(bean);
                            reflashAdapter(listData.size());
                        }
                    }
                });
            }
        } else {
            for (int i = 0; i < queryData.size(); i++) {
                Poetry poetry = queryData.get(i);
                String p_content = poetry.getP_content();
                if(p_content.contains(inputText)){
                    content = inputText;
                    currentStatus = 2;
                    bean.setStatus(currentStatus);
                    bean.setPoetry(poetry);
                    break;
                }
            }
            bean.setContent(content);
            if(currentStatus != 2){//如果未匹配到，则查询数据库，看是否存在数据
                BmobQuery<Poetry> eq = new BmobQuery<>();
                eq.addWhereContains("p_content", inputText);
                List<BmobQuery<Poetry>> queries = new ArrayList<>();
                queries.add(eq);
                BmobQuery<Poetry> mainQuery = new BmobQuery<>();
                mainQuery.or(queries);
                mainQuery.findObjects(new FindListener<Poetry>() {
                    @Override
                    public void done(List<Poetry> list, BmobException e) {
                        if(e == null){
                            if (list != null && list.size() > 0) {
                                Poetry poetry = list.get(0);
                                currentStatus = 3;
                                bean.setStatus(currentStatus);
                                bean.setPoetry(poetry);
                                listData.add(bean);
                                reflashAdapter(listData.size());
                            }
                        }else{
                            currentStatus = 4;
                            bean.setStatus(currentStatus);
                            bean.setPoetry(null);
                            listData.add(bean);
                            reflashAdapter(listData.size());
                        }
                    }
                });

            } else {
                listData.add(bean);
                reflashAdapter(listData.size());
            }
        }

    }

    private void reflashAdapter(int position) {
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
        FlyiingOrderBean bean = new FlyiingOrderBean();
        bean.setUserType(1);
        if(queryData.size() > 0){
            Poetry poetry = queryData.get(0);
            bean.setPoetry(poetry);
            queryData.remove(0);
        }
        listData.add(bean);
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(listData.size());
        }
        rvContent.smoothScrollToPosition(listData.size());
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }
}
