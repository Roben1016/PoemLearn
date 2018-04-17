package com.roshine.poemlearn.ui.fragments;

import android.os.Bundle;
import android.widget.TextView;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.base.BasePageFragment;
import com.roshine.poemlearn.beans.PoemBean;
import com.roshine.poemlearn.utils.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * @author Roshine
 * @date 2018/4/17 19:15
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public class ConfirmGameFragment extends BasePageFragment {

    @BindView(R.id.tv_poem_title)
    TextView tvPoemTitle;
    @BindView(R.id.tv_poem_author)
    TextView tvPoemAuthor;
    @BindView(R.id.tv_poem_year)
    TextView tvPoemYear;
    @BindView(R.id.tv_poem_content)
    TextView tvPoemContent;
    private int fragmentPosition;
    private int poemType;
    private PoemBean mPoemBean;

    public static ConfirmGameFragment newInstance(int position, int poemType, PoemBean poemBean) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("poemType", poemType);
        args.putSerializable("poemBean", poemBean);
        ConfirmGameFragment fragment = new ConfirmGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirmgame;
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            fragmentPosition = arguments.getInt("position");
            poemType = arguments.getInt("poemType");
            mPoemBean = (PoemBean) arguments.getSerializable("poemBean");
            if (mPoemBean != null) {
                initViews();
            }
        }
    }

    private void initViews() {
        String correctPoem = mPoemBean.getCorrectPoem();
        String correctPoem2 = correctPoem.replaceAll("[，。、？,.?]", "\r\n");
        tvPoemTitle.setText(mPoemBean.getPoemTitle());
        tvPoemAuthor.setText(mPoemBean.getPoemAuthor());
        tvPoemYear.setText(mPoemBean.getPoemYear());
        tvPoemContent.setText(correctPoem2);
    }

    @Override
    public void onPageStart() {

    }

    @Override
    public void onPageEnd() {

    }

    @Override
    public void loadNetData() {

    }
}
