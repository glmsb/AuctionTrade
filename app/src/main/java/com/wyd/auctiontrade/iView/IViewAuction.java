package com.wyd.auctiontrade.iView;

import com.wyd.auctiontrade.bean.ProductBean;

import java.util.List;

/**
 * Description :
 * Created by wyd on 2016/5/17.
 */
public interface IViewAuction {

    /**
     * 更新拍卖中商品List视图
     * <p>
     * author: wyd
     * created at 2016/5/17
     */
    void updateAuctionList(List<ProductBean> list);

}
