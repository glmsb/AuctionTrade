package com.wyd.auctiontrade.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Description :拍卖信息表
 * Created by wyd on 2016/5/28.
 */
public class AuctionInfoBean extends BmobObject {
    private BmobDate AuctionTime; //竞拍时间
    private Double bidPrice;  //应价，竞拍价
    private ProductBean product; //产生拍卖信息的拍品，这里体现的是一对多的关系，一个拍品可能产生多条拍卖信息
    private UserInfoBean bidder; //竞买人，一对一的关系

    public UserInfoBean getBidder() {
        return bidder;
    }

    public void setBidder(UserInfoBean bidder) {
        this.bidder = bidder;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BmobDate getAuctionTime() {
        return AuctionTime;
    }

    public void setAuctionTime(BmobDate auctionTime) {
        AuctionTime = auctionTime;
    }
}
