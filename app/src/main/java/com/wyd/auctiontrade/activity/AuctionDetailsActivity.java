package com.wyd.auctiontrade.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.bean.AuctionInfoBean;
import com.wyd.auctiontrade.bean.ProductBean;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.iView.IViewAuctionDetails;
import com.wyd.auctiontrade.presenter.AuctionDetailsPresenter;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import org.kymjs.kjframe.ui.BindView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import me.iwf.photopicker.PhotoPagerActivity;

public class AuctionDetailsActivity extends MVPBaseActivity<IViewAuctionDetails, AuctionDetailsPresenter> implements IViewAuctionDetails {

    public static final int RESULT_CODE_AUCTION_LIST = 1;

    @BindView(id = R.id.imv_back, click = true)
    private ImageView imvBack;
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.tv_click_action)
    private TextView tvClickAction;
    @BindView(id = R.id.imv_auction_pic, click = true)
    private ImageView imvAuctionPic;
    @BindView(id = R.id.tv_pic_number)
    private TextView tvPicNumber;
    @BindView(id = R.id.tv_latest_price)
    private TextView tvLatestPrice;
    @BindView(id = R.id.tv_time_limit)
    private TextView tvTimeLimit;
    @BindView(id = R.id.ll_do_auction)
    private LinearLayout llDoAuction;
    @BindView(id = R.id.imv_sub, click = true)
    private ImageView imvSub;
    @BindView(id = R.id.et_add_price)
    private EditText etAddPrice;
    @BindView(id = R.id.imv_add, click = true)
    private ImageView imvAdd;
    @BindView(id = R.id.tv_bid_price)
    private TextView tvBidPrice;
    @BindView(id = R.id.tv_auction, click = true)
    private TextView tvAuction;
    @BindView(id = R.id.tv_auction_description)
    private TextView tvAuctionDescription;

    private Context mContext;
    private ProductBean productBean;
    private Boolean hasNewPrice; //是否已经有竞拍价
    private Double reservePrice; // 起拍价
    private Double bidPrice;     //竞拍价
    private int addBidPrice;   //增加的筹码

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_auction_details);
    }

    @Override
    public void initData() {
        super.initData();
        mContext = AuctionDetailsActivity.this;
        productBean = (ProductBean) getIntent().getSerializableExtra("ProductBean");
        hasNewPrice = false;
        /** 获取拍卖信息*/
        mPresenter.getAuctionInfo(productBean.getObjectId());
        if (!hasNewPrice) {
            /** 初始化各种价格*/
            reservePrice = productBean.getReservePrice();
            addBidPrice = Integer.parseInt(etAddPrice.getText().toString());
            bidPrice = reservePrice + addBidPrice;
        }
        /** 访问次数加1*/
        productBean.increment("accessTimes");
        productBean.update(mContext);
        /** 倒计时*/
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        try {
            Date endTime = df.parse(productBean.getEndTime().getDate());
            long diff = endTime.getTime() - currentTime.getTime();
            MyCountDown mCount = new MyCountDown(diff, 1000);
            mCount.start();
            if (diff <= 0) {
                mCount.onFinish();
            }
        } catch (Exception e) {
            LogUtil.e("时间转换出错误", e.toString());
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText(productBean.getName());
        tvClickAction.setVisibility(View.GONE);
        etAddPrice.setText("20");
        Glide.with((Activity) mContext)
                .load(productBean.getPicUrl().get(0))
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_photo_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .into(imvAuctionPic);
        String picNumber = String.format(getString(R.string.text_pic_number), productBean.getPicUrl().size());
        tvPicNumber.setText(picNumber);
        tvAuctionDescription.setText(productBean.getDescription());
        /** 如果拍卖信息表中没有数据时才显示携带进来的数据*/
        if (!hasNewPrice) {
            tvLatestPrice.setText("￥" + reservePrice);
            tvBidPrice.setText("￥" + bidPrice);
        }
        /** 自己不能参与自己拍品的竞拍*/
        if (!getIntent().getBooleanExtra("showDoAuction", true)
                || productBean.getAuctionFirm().getObjectId().equals(BmobUser.getCurrentUser(mContext).getObjectId())) {
            llDoAuction.setVisibility(View.GONE);
        } else {
            llDoAuction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.imv_back:
                if (hasNewPrice) {
//                    Intent intent = new Intent();
//                    intent.putExtra("reservePrice", reservePrice);
                    setResult(RESULT_CODE_AUCTION_LIST);
                }
                this.finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.imv_auction_pic:
                Intent intent = new Intent(mContext, PhotoPagerActivity.class);
                intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, 0);
                intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, (Serializable) productBean.getPicUrl());
                intent.putExtra(PhotoPagerActivity.EXTRA_SHOW_DELETE, false);
                startActivity(intent);
                break;
            case R.id.imv_sub:
                addBidPrice = Integer.parseInt(etAddPrice.getText().toString());
                double tempPrice = bidPrice - addBidPrice;
                if (tempPrice > reservePrice) {
                    bidPrice = tempPrice;
                    tvBidPrice.setText("￥" + bidPrice);
                } else {
                    UiUtil.showToast(mContext, "不能再减少拍卖金额了！");
                }
                break;
            case R.id.imv_add:
                addBidPrice = Integer.parseInt(etAddPrice.getText().toString());
                bidPrice = bidPrice + addBidPrice;
                tvBidPrice.setText("￥" + bidPrice);
                break;
            case R.id.tv_auction:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("你给出竞拍出价为:￥" + bidPrice + ",确定立即竞拍吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuctionInfoBean reqParams = new AuctionInfoBean();
                        UserInfoBean user = BmobUser.getCurrentUser(mContext, UserInfoBean.class);
                        reqParams.setBidder(user);
                        reqParams.setBidPrice(bidPrice);
                        Date date = new Date();
                        reqParams.setAuctionTime(new BmobDate(date));
                        ProductBean product = new ProductBean();
                        product.setObjectId(productBean.getObjectId());
                        reqParams.setProduct(product);
                        mPresenter.doAuction(reqParams);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    protected AuctionDetailsPresenter createdPresenter() {
        return new AuctionDetailsPresenter(this);
    }

    @Override
    public void refreshView(List<AuctionInfoBean> auctionList) {
        if (auctionList.size() <= 0) {
            hasNewPrice = false;
            return;
        }
        StringBuilder StrPrice = new StringBuilder();
        for (AuctionInfoBean auctionInfo : auctionList) {
            StrPrice.append(" --->￥");
            StrPrice.append(auctionInfo.getBidPrice());
        }
        StrPrice.delete(0, 6);
        hasNewPrice = true;
        reservePrice = auctionList.get(auctionList.size() - 1).getBidPrice();
        tvLatestPrice.setText(StrPrice);
        addBidPrice = Integer.parseInt(etAddPrice.getText().toString());
        bidPrice = reservePrice + addBidPrice;
        tvBidPrice.setText("￥" + bidPrice);
    }


    class MyCountDown extends CountDownTimer {
        /**
         * @param millisInFuture    倒计的时间数
         * @param countDownInterval onTick事件响应的间隔时间
         */
        public MyCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * @param millisUntilFinished 直到完成的时间
         */
        @Override
        public void onTick(long millisUntilFinished) {
            long hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (millisUntilFinished % (1000 * 60)) / (1000);
            tvTimeLimit.setText(hours + "小时  " + minutes + "分钟  " + seconds + "秒");
        }

        @Override
        public void onFinish() {
            ProductBean reqParam = new ProductBean();
            tvTimeLimit.setText("0小时 0分钟 0秒");
            llDoAuction.setVisibility(View.GONE);
            reqParam.setExpired(true);
            if (hasNewPrice) {
                reqParam.setHammerPrice(reservePrice); //竞拍结束之后把成交价添加到product表中
                mPresenter.sentSMS(productBean.getAuctionFirm().getMobilePhoneNumber(), "smsNotice");
            }
            mPresenter.updateProductBean(productBean.getObjectId(), reqParam);
        }
    }
}
