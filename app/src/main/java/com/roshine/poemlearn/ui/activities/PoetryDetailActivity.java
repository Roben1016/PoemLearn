package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.Config;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.beans.PoetryHistory;
import com.roshine.poemlearn.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Roshine
 * @date 2018/4/19 21:30
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc 诗词详情
 */
public class PoetryDetailActivity extends BaseToolBarActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_poem_title)
    TextView tvPoemTitle;
    @BindView(R.id.tv_poem_author)
    TextView tvPoemAuthor;
    @BindView(R.id.tv_poem_year)
    TextView tvPoemYear;
    @BindView(R.id.tv_poem_content)
    TextView tvPoemContent;
    private String p_id;
    private int from;//0是从历史记录过来， 1是从诗词背诵列表进来  2是从搜索进来

    @Override
    protected int getLayoutId() {
        return R.layout.activity_poetry_detail;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                p_id = extras.getString("p_id");
                from = extras.getInt("from");
            }
        }
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.poetry_detail));
        initData();
    }

    private void initData() {
        showProgress();
        BmobQuery<Poetry> poetryBmobQuery = new BmobQuery<>();
        poetryBmobQuery.getObject(p_id, new QueryListener<Poetry>() {
            @Override
            public void done(Poetry poetry, BmobException e) {
                if(e == null){
                    loadSuc(poetry);
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

    private void loadSuc(Poetry poetry) {
        hideProgress();
        initViews(poetry);
        if(from != 0){
            saveHistory(poetry);
        }
    }

    private void initViews(Poetry poetry) {
        String correctPoem = poetry.getP_content();
        String correctPoem2 = correctPoem.replaceAll("[，。、？！!,.?]", "\r\n");
        tvPoemTitle.setText(poetry.getP_name());
        tvPoemAuthor.setText(poetry.getP_author());
        tvPoemYear.setText(poetry.getP_source());
        tvPoemContent.setText(correctPoem2);
    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
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
