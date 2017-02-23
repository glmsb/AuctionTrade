package com.wyd.auctiontrade.presenter;

import android.content.Context;

import com.wyd.auctiontrade.bean.ProductBean;
import com.wyd.auctiontrade.iView.IViewAuction;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Description :
 * Created by wyd on 2016/5/17.
 */
public class AuctionPresenter extends BasePresenter<IViewAuction> {
    private Context mContext;
    private IViewAuction iViewAuction;

    public AuctionPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void getAuctionList(int page,int stateRefresh) {
        UiUtil.showRoundProcessDialog(mContext, true);
        BmobQuery<ProductBean> productBeanBmobQuery = new BmobQuery<>();
        productBeanBmobQuery.order("-accessTimes,-createdAt");
        productBeanBmobQuery.include("auctionFirm");// 希望在拍品信息的同时也把发布人的信息查询出来
        productBeanBmobQuery.addWhereEqualTo("isExpired", false);//只展示出没有过期的拍品
        if (stateRefresh == 1) {// 如果是加载更多
            productBeanBmobQuery.setSkip(page * 10);
        }
        productBeanBmobQuery.setLimit(10); // 限制最多12条数据结果作为一页
        productBeanBmobQuery.findObjects(mContext, new FindListener<ProductBean>() {
            @Override
            public void onSuccess(List<ProductBean> list) {
                LogUtil.e("getAuctionList", "共" + list.size() + "条数据");
                iViewAuction.updateAuctionList(list);
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("getAuctionList", i + s);
                UiUtil.showToast(mContext, "查询失败");
                UiUtil.dismissProcessDialog();
            }
        });
    }

    @Override
    public void initData() {
        this.iViewAuction = getView();
    }

    public void getMyAuctionList(BmobQuery<ProductBean> query) {
        UiUtil.showRoundProcessDialog(mContext, true);
        query.findObjects(mContext, new FindListener<ProductBean>() {
            @Override
            public void onSuccess(List<ProductBean> list) {
                LogUtil.e("getMyAuctionList", list.toString());
                iViewAuction.updateAuctionList(list);
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("getMyAuctionList", i + s);
                UiUtil.showToast(mContext, "查询失败");
                UiUtil.dismissProcessDialog();
            }
        });
    }
}
