package com.roshine.poemlearn.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.beans.Config;
import com.roshine.poemlearn.beans.FlyiingOrderBean;
import com.roshine.poemlearn.beans.Poetry;
import com.roshine.poemlearn.beans.PoetryHistory;
import com.roshine.poemlearn.utils.LogUtil;
import com.roshine.poemlearn.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author L
 * @date 2018/4/19 21:30
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
    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_title_right)
    ImageView ivSetting;
    @BindView(R.id.rl_details)
    RelativeLayout rlDetails;
    @BindView(R.id.tv_error_title)
    TextView tvErrorTitle;
    @BindView(R.id.tv_error_content)
    TextView tvErrorContent;
    @BindView(R.id.rl_error_poetry)
    RelativeLayout rlErrorPoetry;
    private String p_id;
    private int from;//0是从历史记录过来， 1是从诗词背诵列表进来  2是从搜索进来   3 从飞花令列表进入
    private SpeechSynthesizer mTts;
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    private String currentText;
    private int playStatus;//播放状态
    private SharedPreferences mSharedPreferences;
    private String[] mPersonValues;

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
                FlyiingOrderBean poety = (FlyiingOrderBean) extras.getSerializable("poety");
                if (from == 3) {
                    if (poety != null) {
                        Poetry poetry = poety.getPoetry();
                        if (poetry != null) {
                            loadSuc(poetry);
                        }else {
                            initErrorView(poety);
                        }
                    }else{
                        initErrorView(poety);
                    }
                } else {
                    initData();
                }
            }
        }
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.poetry_detail));
        ivSetting.setVisibility(View.VISIBLE);
        ivSetting.setImageDrawable(getResources().getDrawable(R.drawable.iv_setting));
        mPersonValues = getResources().getStringArray(R.array.voicer_cloud_entries);
        initXunfei();
    }

    private void initErrorView(FlyiingOrderBean poety) {
        rlDetails.setVisibility(View.GONE);
        rlErrorPoetry.setVisibility(View.VISIBLE);
        tvErrorTitle.setText(getResources().getString(R.string.flying_order_no_poetry));
        tvErrorContent.setText(poety == null?getResources().getString(R.string.get_no_poetry):poety.getContent());
    }

    private void initXunfei() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(PlaySettingActivity.PREFER_NAME, MODE_PRIVATE);
    }

    private void initData() {
        showProgress();
        BmobQuery<Poetry> poetryBmobQuery = new BmobQuery<>();
        poetryBmobQuery.getObject(p_id, new QueryListener<Poetry>() {
            @Override
            public void done(Poetry poetry, BmobException e) {
                if (e == null) {
                    loadSuc(poetry);
                } else {
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
        if (from != 0) {
            saveHistory(poetry);
        }
    }

    private void initViews(Poetry poetry) {
        rlErrorPoetry.setVisibility(View.GONE);
        rlDetails.setVisibility(View.VISIBLE);
        StringBuffer stringBuffer = new StringBuffer();
        String pTitle = poetry.getP_name();
        String pAuthor = poetry.getP_author();
        String pYear = poetry.getP_source();
        String correctPoem = poetry.getP_content();
        currentText = stringBuffer.append(pTitle)
                .append("，")
                .append(pAuthor)
                .append("，")
                .append(pYear)
                .append("，")
                .append(correctPoem).toString();
        String correctPoem2 = correctPoem.replaceAll("[，。、？！!,.?]", "\r\n");
        tvPoemTitle.setText(pTitle);
        tvPoemAuthor.setText(pAuthor);
        tvPoemYear.setText(pYear);
        tvPoemContent.setText(correctPoem2);
    }

    @OnClick(R.id.iv_back)
    void backClick() {
        finish();
    }

    @OnClick(R.id.iv_title_right)
    void settingClick() {
        startActivity(PlaySettingActivity.class);
    }

    @OnClick(R.id.tv_play)
    void playClick() {
        if (null == mTts) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip(getResources().getString(R.string.init_xunfei_fail));
            return;
        }

        switch (playStatus) {
            case 0://第一次进入时的默认状态
                if (StringUtils.isEmpty(currentText)) {
                    toast(getResources().getString(R.string.data_init_no));
                    return;
                }
                // 设置参数
                setParam();
                int code = mTts.startSpeaking(currentText, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.ico";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

                if (code != ErrorCode.SUCCESS) {
                    showTip(getResources().getString(R.string.init_xunfei_fail3));
                }
                break;
            case 1://播放状态
                mTts.pauseSpeaking();
                break;
            case 2://暂停状态
                mTts.resumeSpeaking();
                break;
            default:
                break;
        }
    }

    //设置语音参数
    private void setParam() {
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人,可在values/string.xml中选择合适的人的声音
        mTts.setParameter(SpeechConstant.VOICE_NAME, mPersonValues[Integer.valueOf(mSharedPreferences.getString("person_preference", "0"))]);
        //设置合成语速,可设置(0-100)
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调,可设置(0-100)
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量,可设置(0-100)
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
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
                if (e == null) {
                    LogUtil.show("更新历史成功");
                } else {
                    LogUtil.show("更新历史失败");
                    history.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                LogUtil.show("保存历史成功");
                            } else {
                                LogUtil.show("保存历史失败");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            LogUtil.show("InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                toast(getResources().getString(R.string.init_xunfei_fail2));
                LogUtil.show("初始化语音失败，code：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            playStatus = 1;
            tvPlay.setText(getResources().getString(R.string.pause_text));
            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.iv_pause));
            showTip(getResources().getString(R.string.play_start));
        }

        @Override
        public void onSpeakPaused() {
            playStatus = 2;
            tvPlay.setText(getResources().getString(R.string.play_text));
            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.iv_play));
            showTip(getResources().getString(R.string.play_pause));
        }

        @Override
        public void onSpeakResumed() {
            playStatus = 1;
            tvPlay.setText(getResources().getString(R.string.pause_text));
            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.iv_pause));
            showTip(getResources().getString(R.string.play_continue));
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip(getResources().getString(R.string.play_complete));
                playStatus = 0;
                tvPlay.setText(getResources().getString(R.string.replay_text));
                ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.iv_play));
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void showTip(String message) {
        toast(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

}
