package com.wyd.auctiontrade.iView;

import com.wyd.auctiontrade.bean.AuctionInfoBean;

import java.util.List;

/**
 * Description :
 * Created by wyd on 2016/5/22.
 */
public interface IViewAuctionDetails {
    /**
     * 刷新竞拍详情页面
     *
     * @param auctionList 拍卖信息数据
     */
    void refreshView(List<AuctionInfoBean> auctionList);
}
