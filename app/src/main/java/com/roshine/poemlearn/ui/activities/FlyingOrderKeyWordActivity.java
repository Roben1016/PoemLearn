package com.roshine.poemlearn.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.ui.adapters.KeywordAdapter;
import com.roshine.poemlearn.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author L
 * @desc 飞花令选择关键字
 */
public class FlyingOrderKeyWordActivity extends BaseToolBarActivity implements AdapterView.OnItemClickListener, TextWatcher {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_input_keyword)
    EditText etInputKeyword;
    @BindView(R.id.gv_keyword)
    GridView gvKeyword;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private String[] keywordArray;
    private KeywordAdapter keywordAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_keyword;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.set_keyword));
        initGridView();
    }

    private void initGridView() {
        keywordArray = getResources().getStringArray(R.array.keywords);
        keywordAdapter = new KeywordAdapter(this, keywordArray);
        gvKeyword.setAdapter(keywordAdapter);
        gvKeyword.setOnItemClickListener(this);
        etInputKeyword.addTextChangedListener(this);
    }

    //点击返回按钮，退出
    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

    //网格点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        etInputKeyword.setText(keywordArray[position]);
        etInputKeyword.setSelection(keywordArray[position].length());
    }

    //当输入框文本改变时的三个监听回调
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //先重置所有文字为未选中状态
        gvKeyword.setItemChecked(-1,true);
        gvKeyword.clearChoices();
        //再遍历，如果列表里有和输入的文字一样的，则设置为选中状态
        for (int j = 0; j < keywordArray.length; j++) {
            if(charSequence.toString().equals(keywordArray[j])){
                gvKeyword.setItemChecked(j,true);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    //点击确定按钮
    @OnClick(R.id.btn_sure)
    void sureClick() {
        if(TextUtils.isEmpty(etInputKeyword.getText())){
            toast(getResources().getString(R.string.keyword_nll));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("keyword",etInputKeyword.getText().toString());
        startActivity(FlyingOrderActivity.class,bundle);
    }
}
