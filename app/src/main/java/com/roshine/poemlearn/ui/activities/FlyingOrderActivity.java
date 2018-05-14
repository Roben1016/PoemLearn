package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
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

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.FlyiingOrderBean;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.utils.JsonParser;
import com.roshine.poemlearn.utils.LogUtil;
import com.roshine.poemlearn.utils.StringUtils;
import com.roshine.poemlearn.utils.ToastUtil;
import com.roshine.poemlearn.widgets.recyclerview.base.SimpleRecyclertViewAdater;
import com.roshine.poemlearn.widgets.recyclerview.base.ViewHolder;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private RecognizerDialog mIatDialog;
    // 语音听写对象
    private SpeechRecognizer mIat;
    int ret = 0; // 函数调用返回值
    private boolean mTranslateEnable = false;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

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
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        mIatDialog = new RecognizerDialog(this, mInitListener);
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
                                tvInfo.setText(getResources().getString(R.string.chongfu_text));
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
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, ViewHolder holder) {
                if (listData != null && position < listData.size()) {
                    FlyiingOrderBean bean = listData.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("poety",bean);
                    bundle.putInt("from",3);
                    startActivity(PoetryDetailActivity.class,bundle);
                }
            }
        });
    }

    private void query(String keyword) {
        isSuccess = false;
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
                    queryData.clear();
                    if (list != null && list.size() > 0) {
                        isSuccess = true;
                        queryData.addAll(list);
                    } else {
                        isSuccess = false;
                        ToastUtil.showShort(getResources().getString(R.string.get_no_datas));
                        finish();
                    }
                    LogUtil.show("查询数量："+queryData.size());
                }else{
                    isSuccess = false;
                    queryData.clear();
                    ToastUtil.showShort(getResources().getString(R.string.get_data_failed));
                    finish();
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
        if(!isSuccess){
            ToastUtil.showShort(getResources().getString(R.string.loading_data));
            return;
        }
        if(queryData.size() <= 0){
            ToastUtil.showShort(getResources().getString(R.string.no_match_data));
            return;
        }
        showProgress();
        String inputText = etInputPoetry.getText().toString();
        String inputText2 = StringUtils.replaceBiaodian(inputText," ").trim();//替换所有标点为空格
        String inputText3 = inputText2.replaceAll("[' ']+", "-").trim();
        FlyiingOrderBean bean = new FlyiingOrderBean();
        bean.setUserType(0);
        bean.setRoundCount((listData.size() / 2 ) + 1);
        String content = inputText3.replaceAll("-","\r\n");
        currentStatus = 0;
        for (int i = 0; i < inputList.size(); i++) {
            String text = inputList.get(i);
            if (text.equals(inputText3)) {
                currentStatus = 5;
                bean.setPoetry(null);
                bean.setContent(content);
                bean.setStatus(currentStatus);
                reflashAdapter(listData.size());
                return;
            }
        }
        inputList.add(inputText3);
        if(inputText3.contains("-")){
            String[] split = inputText3.split("-");
            for (int i = 0; i < queryData.size(); i++) {
                Poetry poetry = queryData.get(i);
                String p_content = poetry.getP_content();
                String result = StringUtils.replaceBiaodian(p_content," ").trim().replaceAll("[' ']+", "-").trim();
                if(result.contains(inputText3)){
//                    content = result.replaceAll("-","\r\n");
                    LogUtil.show("完全匹配");
                    bean.setPoetry(poetry);
                    currentStatus = 1;
                    bean.setStatus(currentStatus);
                    queryData.remove(i);//匹配一条后移除该数据
                    break;
                }
            }
            bean.setContent(content);
            if(currentStatus != 1){
                LogUtil.show("状态不为1，去数据库查询");
                if(split.length > 0){
                    BmobQuery<Poetry> eq = new BmobQuery<>();
                    eq.addWhereContains("p_content", split[0]);
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
                                    String result = StringUtils.replaceBiaodian(poetry.getP_content()," ").trim().replaceAll("[' ']+", "-").trim();
                                    if(result.contains(inputText3)){
                                        if(inputText3.contains(keyword)){
                                            currentStatus = 1;
                                            bean.setStatus(currentStatus);
                                            bean.setPoetry(poetry);
                                            listData.add(bean);
                                            reflashAdapter(listData.size());
                                        }else{
                                            currentStatus = 3;
                                            bean.setStatus(currentStatus);
                                            bean.setPoetry(poetry);
                                            listData.add(bean);
                                            reflashAdapter(listData.size());
                                        }
                                    }else{
                                        LogUtil.show("不是诗句，不匹配");
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
                    currentStatus = 4;
                    content = inputText;
                    bean.setContent(content);
                    bean.setStatus(currentStatus);
                    bean.setPoetry(null);
                    listData.add(bean);
                    reflashAdapter(listData.size());
                }
            } else {
                bean.setStatus(currentStatus);
                listData.add(bean);
                reflashAdapter(listData.size());
            }

        }else{
            BmobQuery<Poetry> eq = new BmobQuery<>();
            eq.addWhereContains("p_content", inputText2);
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
                            if(inputText.contains(keyword)){
                                currentStatus = 2;
                                bean.setStatus(currentStatus);
                                bean.setPoetry(poetry);
                                bean.setContent(inputText);
                                listData.add(bean);
                                reflashAdapter(listData.size());
                            }else{
                                LogUtil.show("是诗句，但不匹配");
                                currentStatus = 3;
                                bean.setContent(inputText);
                                bean.setStatus(currentStatus);
                                bean.setPoetry(poetry);
                                listData.add(bean);
                                reflashAdapter(listData.size());
                            }
                        } else{
                            currentStatus = 4;
                            bean.setContent(inputText);
                            bean.setStatus(currentStatus);
                            bean.setPoetry(null);
                            listData.add(bean);
                            reflashAdapter(listData.size());
                        }
                    }else{
                        currentStatus = 4;
                        bean.setContent(inputText);
                        bean.setStatus(currentStatus);
                        bean.setPoetry(null);
                        listData.add(bean);
                        reflashAdapter(listData.size());
                    }
                }
            });
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
        etInputPoetry.setText("");
        hideProgress();
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

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void showTip(String message) {
        toast(message);
    }

    //点击录音
    @OnClick(R.id.iv_record)
    void recordClick(){
        if( null == mIat ){
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip( "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化" );
            return;
        }
        etInputPoetry.setText(null);// 清空显示内容
        mIatResults.clear();
        // 设置参数
        setParam();
        // 显示听写对话框
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
        showTip(getString(R.string.text_begin));
    }

    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

//        this.mTranslateEnable = mSharedPreferences.getBoolean(this.getString(R.string.pref_key_translate), false);
//        if (mTranslateEnable) {
//            Log.i(TAG, "translate enable");
//            mIat.setParameter(SpeechConstant.ASR_SCH, "1");
//            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
//            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
//        }

        String lag = "mandarin";
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, lag);

        if (mTranslateEnable) {
            mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
            mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
        }
        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,  "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,  "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
           printResult(results);

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etInputPoetry.setText(resultBuffer.toString());
        etInputPoetry.setSelection(etInputPoetry.length());
    }
}
