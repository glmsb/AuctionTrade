package com.wyd.auctiontrade.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.bean.ProductBean;

/**
 * Description :
 * Created by wyd on 2016/5/21.
 */
public class AuctionListAdapter extends MyBaseAdapter<ProductBean> {

    public AuctionListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_auction_list, null);
            viewHolder.auctionPic = (ImageView) convertView.findViewById(R.id.imv_auction_pic);
            viewHolder.auctionName = (TextView) convertView.findViewById(R.id.tv_auction_name);
            viewHolder.auctionCreateTime = (TextView) convertView.findViewById(R.id.tv_auction_create_time);
            viewHolder.auctionDescription = (TextView) convertView.findViewById(R.id.tv_auction_description);
            viewHolder.priceType = (TextView) convertView.findViewById(R.id.tv_price_type);
            viewHolder.auctionLatestPrice = (TextView) convertView.findViewById(R.id.tv_auction_latest_price);
            viewHolder.auctionAccessTimes = (TextView) convertView.findViewById(R.id.tv_access_times);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(itemList.get(position).getPicUrl().get(0))//只显示图片中第一张照片
                .placeholder(R.drawable.ic_photo_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .into(viewHolder.auctionPic);
        viewHolder.auctionName.setText(itemList.get(position).getName());
        viewHolder.auctionCreateTime.setText(itemList.get(position).getCreatedAt());
        viewHolder.auctionDescription.setText(itemList.get(position).getDescription());
        if (itemList.get(position).getHammerPrice() == null) {
            viewHolder.priceType.setText("实时价格：");
            viewHolder.auctionLatestPrice.setText("￥" + itemList.get(position).getReservePrice());
        } else {
            viewHolder.priceType.setText("成交价格：");
            viewHolder.auctionLatestPrice.setText("￥" + itemList.get(position).getHammerPrice());
        }
        viewHolder.auctionAccessTimes.setText(String.valueOf(itemList.get(position).getAccessTimes()));
        return convertView;
    }

    private class ViewHolder {
        ImageView auctionPic;
        TextView auctionName;
        TextView auctionCreateTime;
        TextView auctionDescription;
        TextView priceType;
        TextView auctionLatestPrice;
        TextView auctionAccessTimes;
    }
}
