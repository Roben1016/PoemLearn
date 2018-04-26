package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.Config;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.beans.PoetryHistory;
import com.roshine.poemlearn.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author L
 * @date 2018/4/18 21:11

 * @desc 诗词填空
 */
public class TianKongActivity extends BaseToolBarActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.name)
    TextView tvPoemName;
    @BindView(R.id.zuozhe)
    TextView tvPoemAuthor;
    @BindView(R.id.ccc)
    LinearLayout llContainer;
    @BindView(R.id.status)
    TextView tvStatus;
    @BindView(R.id.ok)
    Button tvSure;
    @BindView(R.id.next)
    Button next;
    private int poemType;
    private int schoolType;
    private List<Poetry> listData = new ArrayList<>();
    private Poetry current;
    private String[] content;
    private EditText[] editTexts;
    private String[] rights;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tiankong;
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
            tvTitle.setText(getResources().getString(R.string.song_blank_text));
        } else {
            tvTitle.setText(getResources().getString(R.string.tang_blank_text));
        }
        initData();
    }

    private void initData() {
        if(poemType == 1){//查询宋词100条数据
            BmobQuery<Poetry> query = new BmobQuery<Poetry>();
            //查询playerName叫“比目”的数据
            query.addWhereEqualTo("p_type", "宋词");
            //返回50条数据，如果不加上这条语句，默认返回10条数据
            query.setLimit(100);
            //执行查询方法
            query.findObjects(new FindListener<Poetry>() {
                @Override
                public void done(List<Poetry> lists, BmobException e) {
                    if(e==null){
                        LogUtil.show("查询成功：共"+lists.size()+"条数据。");
                        queryDataSuc(lists);
                    }else{
                        LogUtil.show("加载失败："+e.toString());
                        queryDataFailed();
                    }
                }
            });
        }else{
            String strQuery = "小学";
            if(schoolType == 1){
                strQuery = "初中";
            }else if(schoolType == 2){
                strQuery = "高中";
            }else{
                strQuery = "小学";
            }
            showProgress();
            BmobQuery<Poetry> query = new BmobQuery<Poetry>();
            //查询playerName叫“比目”的数据
            query.addWhereEqualTo("p_type", strQuery);
            //返回50条数据，如果不加上这条语句，默认返回10条数据
            query.setLimit(100);
            //执行查询方法
            query.findObjects(new FindListener<Poetry>() {
                @Override
                public void done(List<Poetry> lists, BmobException e) {
                    if(e==null){
                        LogUtil.show("查询成功：共"+lists.size()+"条数据。");
                        queryDataSuc(lists);
                    }else{
                        LogUtil.show("加载失败："+e.toString());
                        queryDataFailed();
                    }
                }
            });
        }
    }

    private void queryDataSuc(List<Poetry> lists) {
        hideProgress();
        if (listData != null) {
            listData.clear();
            listData.addAll(lists);
            next();
        }
    }

    private void queryDataFailed() {
        hideProgress();
        toast(getResources().getString(R.string.load_failed));
    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

    @OnClick(R.id.next)
    void nextClick(){
        next();
    }

    @OnClick(R.id.ok)
    void sureClick(){
        boolean hasError = false;
        for (int i = 0, length = editTexts.length; i < length; i++) {
            if (TextUtils.equals(rights[i], editTexts[i].getText().toString())) {
                editTexts[i].setTextColor(Color.GREEN);
            } else {
                hasError = true;
                editTexts[i].setTextColor(Color.RED);
            }
        }
        if (!hasError) {
            tvStatus.setVisibility(View.VISIBLE);
        }
    }

    private void next() {
        tvStatus.setVisibility(View.GONE);

        Random r = new Random();
        int next = r.nextInt(10000) % listData.size();

        Poetry shici = listData.get(next);

//        while (done.contains(shici)) {
//            next = r.nextInt(10000) % listData.size();
//            shici = listData.get(next);
//        }

        current = shici;

        //保存历史记录
        saveHistory(shici);

        tvPoemName.setText(shici.getP_name());
        tvPoemAuthor.setText(shici.getP_author());
        String replace = shici.getP_content().replaceAll("[，。、？！!,.?]", "-");//将标点替换成-
        String[] split = replace.split("-");
        if(split.length > 0 && split[split.length-1].contains("：")){
            content = new String[split.length-1];
            for (int i = 0; i < split.length -1; i++) {
                content[i] = split[i];
            }
        }else{
            content = split;
        }

        int hang = content.length / 2;
        editTexts = new EditText[hang];
        rights = new String[hang];

        llContainer.removeAllViews();
        for (int i = 0; i < hang; i++) {
            int kong = r.nextInt(100) % 2;
            int layout = -1;
            if (kong == 0) {
                layout = R.layout.tinakong_item1;
            } else {
                layout = R.layout.tinakong_item;
            }
            View v = getLayoutInflater().inflate(layout, null);
            editTexts[i] = (EditText) v.findViewById(R.id.edit);
            if (kong == 0) {
                ((TextView) v.findViewById(R.id.txt)).setText(content[i * 2 + 1]);
                rights[i] = content[i * 2];
            } else {
                ((TextView) v.findViewById(R.id.txt)).setText(content[i * 2]);
                rights[i] = content[i * 2 + 1];
            }
            llContainer.addView(v);
        }
    }

    //保存历史记录
    private void saveHistory(Poetry mPoemBean) {
        PoetryHistory history = new PoetryHistory();
        history.setP_id(mPoemBean.getObjectId());
        history.setP_author(mPoemBean.getP_author());
        history.setP_title(mPoemBean.getP_name());
        history.setU_id(Config.getInstance().user.getObjectId());
        history.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    LogUtil.show("更新历史成功");
                }else{
                    LogUtil.show("更新历史失败");
                    history.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                LogUtil.show("保存历史成功");
                            }else{
                                LogUtil.show("保存历史失败");
                            }
                        }
                    });
                }
            }
        });
    }
}
