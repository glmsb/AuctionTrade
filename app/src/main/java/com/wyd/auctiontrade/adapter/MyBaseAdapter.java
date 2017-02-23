package com.wyd.auctiontrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 自定义基类adapter
 * Created by wyd on 2016/4/23.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> itemList;
    protected LayoutInflater inflater;

    public MyBaseAdapter(Context context) {
        this.mContext = context;
        this.itemList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }


    /**
     * 在原有的数据上添加新数据，并刷新数据集
     *
     * @param itemList 需要新添加的数据
     */
    public void addItems(List<T> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 设置为新的数据，旧数据会被清空（重新设置数据集），并刷新数据集
     *
     * @param itemList 数据集
     */
    public void setItems(List<T> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     * 注意：此方法会清空原有itemList，导致原有的外部list为empty！！！
     */
    public void clearItems() {
        if (itemList != null && itemList.size() > 0) {
            itemList.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 根据下标移除item，并刷新数据集
     *
     * @param index 下标
     */
    public void removeItemByIndex(int index) {
        if (index < itemList.size()) {
            itemList.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
