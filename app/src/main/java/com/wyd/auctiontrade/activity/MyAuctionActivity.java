package com.wyd.auctiontrade.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.adapter.AuctionListAdapter;
import com.wyd.auctiontrade.bean.ProductBean;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.iView.IViewAuction;
import com.wyd.auctiontrade.presenter.AuctionPresenter;

import org.kymjs.kjframe.ui.BindView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

public class MyAuctionActivity extends MVPBaseActivity<IViewAuction, AuctionPresenter> implements IViewAuction {
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.lv_my_auction)
    private ListView lvMyAuction;
    @BindView(id = R.id.rl_empty_view)
    private RelativeLayout rlEmptyView;

    private AuctionListAdapter adapter;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_my_auction);
    }

    @Override
    public void initData() {
        super.initData();
//        ProductBean reqParams = new ProductBean();
//        reqParams.setAuctionFirm(BmobUser.getCurrentUser(this, UserInfoBean.class));
        BmobQuery<ProductBean> query = new BmobQuery<>();
        // 多个排序字段可以用（，）号分隔
        query.order("-isExpired,-createdAt");
        query.addWhereEqualTo("auctionFirm", BmobUser.getCurrentUser(this, UserInfoBean.class));
        mPresenter.getMyAuctionList(query);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText("我的拍品");
    }

    @Override
    protected AuctionPresenter createdPresenter() {
        return new AuctionPresenter(this);
    }

    @Override
    public void updateAuctionList(List<ProductBean> list) {
        if (list != null && list.size() > 0) {
            rlEmptyView.setVisibility(View.GONE);
            lvMyAuction.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new AuctionListAdapter(this);
                adapter.setItems(list);
                lvMyAuction.setAdapter(adapter);
            } else {
                adapter.setItems(list);
            }
            lvMyAuction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyAuctionActivity.this, AuctionDetailsActivity.class);
                    ProductBean productBean = (ProductBean) parent.getAdapter().getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("showDoAuction", false);
                    bundle.putSerializable("ProductBean", productBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            rlEmptyView.setVisibility(View.VISIBLE);
            lvMyAuction.setVisibility(View.GONE);
        }
    }
}
