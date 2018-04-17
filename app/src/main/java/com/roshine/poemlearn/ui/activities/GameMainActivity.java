package com.roshine.poemlearn.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BaseToolBarActivity;
import com.roshine.poemlearn.utils.LogUtil;
import com.roshine.poemlearn.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Roshine
 * @date 2018/4/14 23:11
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class GameMainActivity extends BaseToolBarActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.iv_blank)
    ImageView ivBlank;
    @BindView(R.id.iv_recite)
    ImageView ivRecite;
    @BindView(R.id.iv_poem_game)
    ImageView ivPoemGame;
    @BindView(R.id.rb_primary)
    RadioButton rbPrimary;
    @BindView(R.id.rb_junior)
    RadioButton rbJunior;
    @BindView(R.id.rb_high)
    RadioButton rbHigh;
    @BindView(R.id.rg_games)
    RadioGroup rgGames;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private int poemType;//0是唐诗  1是宋词
    private int schoolType;//0小学  1初中 2高中

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_main;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                poemType = bundle.getInt("poemType");
            }
        }
        setImages();
        rbPrimary.setChecked(true);
        rgGames.setOnCheckedChangeListener(this);
        ivBack.setVisibility(View.VISIBLE);
    }

    private void setImages() {
        switch (poemType) {
            case 0:
                ivBlank.setImageDrawable(getResources().getDrawable(R.drawable.tang_blank));
                ivRecite.setImageDrawable(getResources().getDrawable(R.drawable.tang_recite));
                break;
            case 1:
                ivBlank.setImageDrawable(getResources().getDrawable(R.drawable.song_blank));
                ivRecite.setImageDrawable(getResources().getDrawable(R.drawable.song_recite));
                break;
            default:
                ivBlank.setImageDrawable(getResources().getDrawable(R.drawable.tang_blank));
                ivRecite.setImageDrawable(getResources().getDrawable(R.drawable.tang_recite));
                break;
        }
    }

    @OnClick(R.id.iv_blank)
    void blankClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType",poemType);
        bundle.putInt("schoolType",schoolType);
        startActivity(BlankActivity.class,bundle);
    }

    @OnClick(R.id.iv_recite)
    void reciteClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType",poemType);
        bundle.putInt("schoolType",schoolType);
        startActivity(ReciteActivity.class,bundle);
    }
    //主线程接受信息
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Bundle data = msg.getData();
//            String val = data.getString("content");
//            LogUtil.show("Output Test", "article content is-->" + val);
//            // UI界面的更新等相关操作
//
//            LogUtil.show("....","....");
//            toast(val);
//        }
//    };
    @OnClick(R.id.iv_poem_game)
    void poemGameClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("poemType",poemType);
        bundle.putInt("schoolType",schoolType);
        startActivity(ConfirmGameActivity.class,bundle);
//        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/poetry.db", null);
//        String selectSQL = "select * from person";
//        Cursor cursor = database.rawQuery(selectSQL, null);
//        if(cursor.moveToFirst()) {
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    toast(name);
//                }
//            });
//        }
//        cursor.close();
//        database.close();


//        poemDataBaseHelper = new PoemDataBaseHelper(this);
//        SQLiteDatabase writableDatabase = poemDataBaseHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("name", "jack");
//        cv.put("number", "3");
//        long person = database.insert("person", null, cv);
//        toast(""+person);
        //子线程连接数据库
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Runnable networkTask = new Runnable() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                Bundle data = new Bundle();
//                Statement statement = null;
//                Connection jdbcConnection = null;
//                ResultSet resultSet = null;
//                try {
//                    Class.forName("com.mysql.jdbc.Driver");
//                    //ip地址得随网络改（仅限本人此程序）
//                    String DB_URL = "jdbc:mysql://localhost:3306/poetry";
//                    String username = "root";
//                    String password = "abc123";
//                    jdbcConnection = DriverManager.getConnection(DB_URL, username, password);
//                    statement = jdbcConnection.createStatement();
//                    String sql = "select content from poetry where name = '" +"伤心行"+ "'";
//                    System.out.println(sql);
//                    resultSet = statement.executeQuery(sql);
//
//                    while (resultSet.next()) {
//                       String contents =  resultSet.getString("content");
//                       data.putString("content",contents);
//                        LogUtil.show("Debug", "Start Run Task..."+contents);
//                    }
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (java.sql.SQLException e) {
//                    e.printStackTrace();
//                }finally {
//                    if (jdbcConnection != null) {
//                        try {
//                            jdbcConnection.close();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (resultSet != null) {
//                        try {
//                            resultSet.close();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                 msg.setData(data);
//                handler.sendMessage(msg);
//            }
//        };
//        new Thread(networkTask).start();
//        LogUtil.show("Debug Input", "Search Over");
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_primary:
                schoolType = 0;
                break;
            case R.id.rb_junior:
                schoolType = 1;
                break;
            case R.id.rb_high:
                schoolType = 2;
                break;
            default:
                schoolType = 0;
                break;
        }
    }
    @OnClick(R.id.iv_back)
    void backClick(){
        finish();
    }
}
