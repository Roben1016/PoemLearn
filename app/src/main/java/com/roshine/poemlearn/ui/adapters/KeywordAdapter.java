package com.roshine.poemlearn.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.roshine.poemlearn.R;
import com.roshine.poemlearn.widgets.CustomTextView;


/**
 * @author L
 * @desc 设置关键字单选适配器
 */
public class KeywordAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private String[] datas;
    private Context mContext;

    public KeywordAdapter(Context context,String[] datas){
        this.mContext = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (datas != null) {
            return datas.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (datas != null) {
            return datas[i];
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            // 根布局为我们自定义的LsCheckedableTextView
            convertView = inflater.inflate(R.layout.item_keyword_choose,null);
            holder.checkedableTextView = (CustomTextView) convertView;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkedableTextView.setText(datas[position]);
        return convertView;
    }
    private static class ViewHolder{
        CustomTextView checkedableTextView;
    }
}
