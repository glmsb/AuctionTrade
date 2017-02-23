package com.wyd.auctiontrade.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.activity.MVPBaseActivity;

import org.kymjs.kjframe.utils.ImageUtils;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;


/**
 * Description:展示照片选择结果
 * Created by wyd on 2016/4/26.
 */
public class SelectPicResultAdapter extends MyBaseAdapter<String> {

    public SelectPicResultAdapter(Context context, List<String> photoPaths) {
        super(context);
        this.itemList = photoPaths;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_select_pic_result, viewGroup, false);
            viewHolder.imvSelectPic = (ImageView) convertView.findViewById(R.id.imv_add_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (itemList.get(position).equals("addPic")) {
            viewHolder.imvSelectPic.setImageResource(R.mipmap.ic_add_pic);
        } else {
            //Uri uri = Uri.fromFile(new File(itemList.get(position)));
            Glide.with(mContext)
                    .load(itemList.get(position))
                    .placeholder(R.drawable.ic_photo_black_48dp)
                    .error(R.drawable.ic_broken_image_black_48dp)
                    .into(viewHolder.imvSelectPic);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imvSelectPic;
    }
}
