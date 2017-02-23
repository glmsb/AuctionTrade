package com.wyd.auctiontrade.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Description :商品信息实体
 * Created by wyd on 2016/5/16.
 */
public class ProductBean extends BmobObject implements Serializable {

    private String name;   //名称
    private Double reservePrice;  //起拍价
    private Double hammerPrice;   //成交价（可以为null）
    private String description;  //商品描述
    private BmobDate endTime; //交易结束的时间
    private Integer time;        //拍卖时长（以分钟为单位）
    private Boolean isExpired;    //拍卖是否过期
    private Integer accessTimes;  //访问次数
    private List<String> picUrl; //图片链接地址

    private UserInfoBean auctionFirm; //拍卖人，这里体现的是一对一的关系，该拍品属于某个用户


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }


    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getAccessTimes() {
        return accessTimes;
    }

    public void setAccessTimes(Integer accessTimes) {
        this.accessTimes = accessTimes;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public Double getHammerPrice() {
        return hammerPrice;
    }

    public void setHammerPrice(Double hammerPrice) {
        this.hammerPrice = hammerPrice;
    }

    public Double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(Double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public UserInfoBean getAuctionFirm() {
        return auctionFirm;
    }

    public void setAuctionFirm(UserInfoBean auctionFirm) {
        this.auctionFirm = auctionFirm;
    }
}
