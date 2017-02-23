package com.wyd.auctiontrade.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.activity.AuctionDetailsActivity;
import com.wyd.auctiontrade.adapter.AuctionListAdapter;
import com.wyd.auctiontrade.bean.ProductBean;
import com.wyd.auctiontrade.iView.IViewAuction;
import com.wyd.auctiontrade.presenter.AuctionPresenter;
import com.wyd.auctiontrade.util.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Description :首页
 * Created by wyd on 2016/5/10.
 */
public class AuctionFragment extends MVPBaseFragment<IViewAuction, AuctionPresenter> implements IViewAuction {

    private static final int REQUEST_CODE_JUMP_TO_DETAILS = 1;
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private View rootView;
    private PullToRefreshListView lvAuction;
    private RelativeLayout rlEmptyView;
    private AuctionListAdapter adapter;

    private int actionType = -1;    //操作类型（下拉刷新、上拉加载更多）
    private boolean hasNewData;     //下拉加载时是否还有新数据
    private int curPage = 1;        // 当前页的编号，从1开始


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_auction, container, false);
            initData();
        } else {
            ViewGroup p = (ViewGroup) rootView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }
        return rootView;
    }

    public void initData() {
        lvAuction = (PullToRefreshListView) rootView.findViewById(R.id.lv_auction);
        rlEmptyView = (RelativeLayout) rootView.findViewById(R.id.rl_empty_view);
        initPullRefreshText();
        initPullRefreshView();
        mPresenter.getAuctionList(curPage,actionType);
    }

    private void initPullRefreshText() {
        // 设置下拉刷新的文字
        ILoadingLayout upLoading = lvAuction.getLoadingLayoutProxy(true, false);
        upLoading.setPullLabel(getString(R.string.is_pull_down_refresh));
        upLoading.setReleaseLabel(getString(R.string.is_refresh_start));
        upLoading.setRefreshingLabel(getString(R.string.is_loading));
        // 设置上拉加载的文字
        ILoadingLayout downLoading = lvAuction.getLoadingLayoutProxy(false, true);
        downLoading.setPullLabel(getString(R.string.is_pull_down_load));
        downLoading.setReleaseLabel(getString(R.string.is_pull_load_more));
        downLoading.setRefreshingLabel(getString(R.string.is_loading));
        downLoading.setLoadingDrawable(getDrawableById(R.drawable.default_ptr_flip));
    }

    private void initPullRefreshView() {
        lvAuction.setMode(PullToRefreshBase.Mode.BOTH);
        lvAuction.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {//下拉刷新
                actionType = STATE_REFRESH;
                mPresenter.getAuctionList(curPage, actionType);// 下拉刷新(从第一页开始装载数据)
                lvAuction.post(new Runnable() {
                    @Override
                    public void run() {
                        lvAuction.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {//上拉加载
                if (!hasNewData) { //已经加载全部
                    lvAuction.post(new Runnable() {
                        @Override
                        public void run() {
                            ILoadingLayout loading = lvAuction.getLoadingLayoutProxy(
                                    false, true);
                            loading.setReleaseLabel(getString(R.string.is_loaded_all));
                            loading.setPullLabel(getString(R.string.is_loaded_all));
                            loading.setRefreshingLabel(getString(R.string.is_loaded_all));
                            loading.setLoadingDrawable(null);
                            lvAuction.onRefreshComplete();
                        }
                    });
                } else {//加载下一页
                    initPullRefreshText();
                    actionType = STATE_MORE;
                    mPresenter.getAuctionList(curPage, actionType);// 上拉加载更多(加载下一页数据)
                    lvAuction.post(new Runnable() {
                        @Override
                        public void run() {
                            lvAuction.onRefreshComplete();
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_JUMP_TO_DETAILS && resultCode == AuctionDetailsActivity.RESULT_CODE_AUCTION_LIST) {
            mPresenter.getAuctionList(curPage,actionType);
        }
    }

    @Override
    public void updateAuctionList(List<ProductBean> list) {
        if (list != null && list.size() > 0) {
            rlEmptyView.setVisibility(View.GONE);
            lvAuction.setVisibility(View.VISIBLE);

            if (actionType == STATE_REFRESH) {
                curPage = 1;
            } else if (actionType == STATE_MORE) {
                curPage++;
            }
            hasNewData = list.size() == 10;
            if (adapter == null) {
                adapter = new AuctionListAdapter(mActivity);
                adapter.setItems(list);
                lvAuction.setAdapter(adapter);
            } else {
                adapter.setItems(list);
            }
            lvAuction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mActivity, AuctionDetailsActivity.class);
                    ProductBean productBean = (ProductBean) parent.getAdapter().getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ProductBean", productBean);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE_JUMP_TO_DETAILS);
                }
            });
        } else {
            rlEmptyView.setVisibility(View.VISIBLE);
            lvAuction.setVisibility(View.GONE);
        }
    }

    @Override
    protected AuctionPresenter createdPresenter() {
        return new AuctionPresenter(mActivity);
    }
}
