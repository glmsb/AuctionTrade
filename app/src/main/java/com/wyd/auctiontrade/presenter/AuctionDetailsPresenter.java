package com.wyd.auctiontrade.presenter;

import android.content.Context;

import com.wyd.auctiontrade.bean.AuctionInfoBean;
import com.wyd.auctiontrade.bean.ProductBean;
import com.wyd.auctiontrade.iView.IViewAuctionDetails;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Description :
 * Created by wyd on 2016/5/22.
 */
public class AuctionDetailsPresenter extends BasePresenter<IViewAuctionDetails> {
    private Context mContext;
    private IViewAuctionDetails iViewAuctionDetails;

    public AuctionDetailsPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void initData() {
        this.iViewAuctionDetails = getView();
    }

    public void getAuctionInfo(String objectId) {
        UiUtil.showRoundProcessDialog(mContext, true);
        BmobQuery<AuctionInfoBean> query = new BmobQuery<>();
        ProductBean product = new ProductBean();
        product.setObjectId(objectId);
        query.addWhereEqualTo("product", new BmobPointer(product));
        //希望同时查询该拍卖信息，以及该拍卖的买主信息和拍卖品，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("bidder,product.endTime");
        query.findObjects(mContext, new FindListener<AuctionInfoBean>() {
            @Override
            public void onSuccess(List<AuctionInfoBean> list) {
                LogUtil.e("getAuctionInfo", list.toString());
                iViewAuctionDetails = getView();
                iViewAuctionDetails.refreshView(list);
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("getAuctionInfo", i + s);
                UiUtil.showToast(mContext, "查询失败");
                UiUtil.dismissProcessDialog();
            }
        });
    }

    public void doAuction(final AuctionInfoBean auctionInfo) {
        auctionInfo.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.e("doAuction", "竞拍成功");
                getAuctionInfo(auctionInfo.getProduct().getObjectId());
                ProductBean reqParam = new ProductBean();
                reqParam.setReservePrice(auctionInfo.getBidPrice()); //竞拍结束之后把成交价添加到product表中
                updateProductBean(auctionInfo.getProduct().getObjectId(), reqParam);
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e("doAuction", i + s);
            }
        });
    }

    public void updateProductBean(String objectId, ProductBean reqParam) {
        reqParam.update(mContext, objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                LogUtil.e("updateProductBean", "更新成功");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e("updateProductBean", i + s);
            }
        });
    }

    public void sentSMS(String mobilePhoneNumber, String auctionsms) {
//        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        String sendTime = format.format(Calendar.getInstance().getTime());
        BmobSMS.requestSMSCode(mContext, mobilePhoneNumber, auctionsms, new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {
                    LogUtil.e("bmob", "短信发送成功，短信id：" + smsId);//用于查询本次短信发送详情
                } else {
                    LogUtil.e("bmob", "errorCode = " + ex.getErrorCode() + ",errorMsg = " + ex.getLocalizedMessage());
                }
            }
        });
    }
}
