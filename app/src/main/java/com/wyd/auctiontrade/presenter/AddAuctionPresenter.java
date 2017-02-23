package com.wyd.auctiontrade.presenter;

import android.content.Context;

import com.wyd.auctiontrade.bean.ProductBean;
import com.wyd.auctiontrade.iView.IVIewAddAuction;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Description :
 * Created by wyd on 2016/5/16.
 */
public class AddAuctionPresenter extends BasePresenter<IVIewAddAuction> {
    private Context mContext;
    private IVIewAddAuction iViewAuction;

    public AddAuctionPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void initData() {
        this.iViewAuction = getView();
    }

    public void addAuction(ProductBean productBean) {
        /**
         *  向服务器添加数据
         */
        productBean.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.e("addAuction", "发布成功！！");
                UiUtil.showToast(mContext, "发布成功！！");
                iViewAuction = getView();
                iViewAuction.returnAuctionList();
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e("addAuction", "发布失败" + s);
                UiUtil.showToast(mContext, "发布失败" + s);
                UiUtil.dismissProcessDialog();
            }
        });
    }


    public void uploadPic(final String[] fileList, final ProductBean productBean) {
        UiUtil.showRoundProcessDialog(mContext, false);
        BmobFile.uploadBatch(mContext, fileList, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                if (urls.size() == fileList.length) {//如果全部上传完，则更新该条记录
                    productBean.setPicUrl(urls);
                    addAuction(productBean);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int statusCode, String errorMsg) {
                UiUtil.showToast(mContext, "错误码" + statusCode + ",错误描述：" + errorMsg);
                UiUtil.dismissProcessDialog();
            }
        });
    }
}
