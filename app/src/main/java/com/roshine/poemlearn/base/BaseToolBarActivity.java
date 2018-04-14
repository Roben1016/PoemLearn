package com.roshine.poemlearn.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.roshine.poemlearn.utils.StatusBarUtil;


/**
 * @author Roshine
 * @date 2018/4/14 20:56
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc 带toolbar的activity,设置沉浸式状态栏
 */
public abstract class BaseToolBarActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //设置全屏
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
//        StatusBarUtil.setColorBar(this, getResources().getColor(ThemeColorUtil.getThemeColor()));
    }

//    public void initAppBarLayout(AppBarLayout appBar) {
//        if (appBar == null) return;
//        if (Build.VERSION.SDK_INT >= 21) {
//            appBar.setElevation(DisplayUtil.dp2px(this,3));
//        }
//    }

//    @Override
//    protected void initViewData(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return 0;
//    }
}
