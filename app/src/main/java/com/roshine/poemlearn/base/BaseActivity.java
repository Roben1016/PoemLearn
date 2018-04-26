package com.roshine.poemlearn.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.utils.ActivityUtil;
import com.roshine.poemlearn.utils.DisplayUtil;
import com.roshine.poemlearn.utils.ToastUtil;
import com.roshine.poemlearn.widgets.CustomProgressDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author L
 * @date 2018/4/14 20:44

 * @desc 所有activity通用基类
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView{
    protected Activity activity;
    private Unbinder unbinder;
    protected int screenWidth;
    protected int screenHeight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenHeight = DisplayUtil.getScreenHeight(this);
        screenWidth = DisplayUtil.getScreenWidth(this);
        ActivityUtil.getInstance().addActivity(this);//添加activity栈
        activity = this;
        if(getLayoutId() != 0){
            setContentView(getLayoutId());
            unbinder = ButterKnife.bind(this);
        }
        if (isEventBus()) {
            EventBus.getDefault().register(this);
        }
        initViewData(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void initViewData(Bundle savedInstanceState);

    @Override
    public void showProgress() {
        showProgress(this.getString(R.string.loading_text),false);
    }

    @Override
    public void showProgress(String message) {
        showProgress(message,false);
    }

    @Override
    public void showProgress(String message, boolean cancelable) {
        CustomProgressDialog.showLoading(this,message,cancelable);
    }

    @Override
    public void hideProgress() {
        CustomProgressDialog.stopLoading();
    }

    @Override
    public void toast(String message) {
        ToastUtil.showSingleToast(message);
    }

    @Override
    public void toastLong(String message) {
        ToastUtil.showLong(message);
    }

    @Override
    public void toastWithTime(int time, String message) {
        ToastUtil.showWithTime(message,time);
    }

    @Override
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    @Override
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.getInstance().removeActivity(this);//移除activity栈
        if (unbinder != null && unbinder != Unbinder.EMPTY) unbinder.unbind();
        this.unbinder = null;
        if (isEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected boolean isEventBus(){
        return false;
    }
}
