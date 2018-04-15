package com.roshine.poemlearn.widgets.recyclerview.base;

import android.content.Context;

import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemClickListener;
import com.roshine.poemlearn.widgets.recyclerview.interfaces.OnItemLongClickListener;

import java.util.List;

/**
 * @author Roshine
 * @date 2018/4/15 10:27
 * @blog http://www.roshine.xyz
 * @email roshines1016@gmail.com
 * @github https://github.com/Roben1016
 * @phone 136****1535
 * @desc
 */
public abstract class SimpleRecyclertViewAdater<T> extends BaseRecyclertViewAdapter {
    private int mLayoutId;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
//    private boolean singleClicFlag = true;//单击事件和长单击事件的屏蔽标识
    public SimpleRecyclertViewAdater(Context context, List<T> listData, int layoutId) {
        super(context, listData);
        mLayoutId = layoutId;
    }

    @Override
    protected int getLayoutIdFromType(int itemType) {
        return mLayoutId;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, int itemType, Object itemBean) {
        onBindViewHolder(holder,itemType,(T)itemBean,position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
//                if(singleClicFlag){
                    mOnItemClickListener.OnItemClick(position,holder);
//                }
//                singleClicFlag = true;
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                mOnItemLongClickListener.onItemLongClick(position,holder);
//                singleClicFlag = false;
                return false;
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    //子类重写该方法
    protected abstract void onBindViewHolder(ViewHolder holder,int itemType, T itemBean, int position);

    //设置单击事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    //设置长按事件
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
