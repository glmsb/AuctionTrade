package com.wyd.auctiontrade.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.activity.AddAuctionActivity;
import com.wyd.auctiontrade.bean.DreamBean;

import cn.bmob.v3.BmobUser;

/**
 * Description :
 * Created by wyd on 2016/5/28.
 */
public class DreamListAdapter extends MyBaseAdapter<DreamBean> {

    public DreamListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_dream_list, null);
            viewHolder.dreamName = (TextView) convertView.findViewById(R.id.tv_dream_name);
            viewHolder.dreamCreateTime = (TextView) convertView.findViewById(R.id.tv_dream_create_time);
            viewHolder.dreamDescription = (TextView) convertView.findViewById(R.id.tv_dream_description);
            viewHolder.dreamType = (TextView) convertView.findViewById(R.id.tv_dream_type);
            viewHolder.tvAchieve = (TextView) convertView.findViewById(R.id.tv_achieve);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.dreamName.setText(itemList.get(position).getName());
        viewHolder.dreamCreateTime.setText(itemList.get(position).getCreatedAt());
        viewHolder.dreamDescription.setText(itemList.get(position).getDescription());
        viewHolder.dreamType.setText(itemList.get(position).getType());

        // itemList.get(position).getUserName().getMobilePhoneNumber();
        if (BmobUser.getCurrentUser(mContext).getObjectId().equals(itemList.get(position).getUserName().getObjectId())) {
            viewHolder.tvAchieve.setVisibility(View.GONE);
        } else {
            viewHolder.tvAchieve.setVisibility(View.VISIBLE);
        }
        viewHolder.tvAchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddAuctionActivity.class);
                intent.putExtra("dreamName", itemList.get(position).getName());
                intent.putExtra("dreamDescription", itemList.get(position).getDescription());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView dreamName;
        TextView dreamCreateTime;
        TextView dreamDescription;
        TextView dreamType;
        TextView tvAchieve;
    }
}
