package com.wyd.auctiontrade.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.adapter.DreamListAdapter;
import com.wyd.auctiontrade.bean.DreamBean;
import com.wyd.auctiontrade.iView.IViewDream;
import com.wyd.auctiontrade.presenter.DreamPresenter;

import java.util.List;

/**
 * Description :拍卖商品
 * Created by wyd on 2016/5/10.
 */
public class DreamFragment extends MVPBaseFragment<IViewDream, DreamPresenter> implements IViewDream {

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private View rootView;
    private RelativeLayout rlEmptyView;
    private PullToRefreshListView lvDream;
    private DreamListAdapter adapter;

    private int actionType = -1;    //操作类型（下拉刷新、上拉加载更多）
    private boolean hasNewData;     //下拉加载时是否还有新数据
    private int curPage = 1;        // 当前页的编号，从1开始

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_dream, container, false);
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
        rlEmptyView = (RelativeLayout) rootView.findViewById(R.id.rl_empty_view);
        lvDream = (PullToRefreshListView) rootView.findViewById(R.id.lv_dream);
        initPullRefreshText();
        initPullRefreshView();
        mPresenter.getDreamList(0, STATE_REFRESH);
    }

    private void initPullRefreshText() {
        // 设置下拉刷新的文字
        ILoadingLayout upLoading = lvDream.getLoadingLayoutProxy(true, false);
        upLoading.setPullLabel(getString(R.string.is_pull_down_refresh));
        upLoading.setReleaseLabel(getString(R.string.is_refresh_start));
        upLoading.setRefreshingLabel(getString(R.string.is_loading));
        // 设置上拉加载的文字
        ILoadingLayout downLoading = lvDream.getLoadingLayoutProxy(false, true);
        downLoading.setPullLabel(getString(R.string.is_pull_down_load));
        downLoading.setReleaseLabel(getString(R.string.is_pull_load_more));
        downLoading.setRefreshingLabel(getString(R.string.is_loading));
        downLoading.setLoadingDrawable(getDrawableById(R.drawable.default_ptr_flip));
    }

    private void initPullRefreshView() {
        lvDream.setMode(PullToRefreshBase.Mode.BOTH);
        lvDream.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {//下拉刷新
                actionType = STATE_REFRESH;
                mPresenter.getDreamList(curPage, actionType);// 下拉刷新(从第一页开始装载数据)
                lvDream.post(new Runnable() {
                    @Override
                    public void run() {
                        lvDream.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {//上拉加载
                if (!hasNewData) { //已经加载全部
                    lvDream.post(new Runnable() {
                        @Override
                        public void run() {
                            ILoadingLayout loading = lvDream.getLoadingLayoutProxy(
                                    false, true);
                            loading.setReleaseLabel(getString(R.string.is_loaded_all));
                            loading.setPullLabel(getString(R.string.is_loaded_all));
                            loading.setRefreshingLabel(getString(R.string.is_loaded_all));
                            loading.setLoadingDrawable(null);
                            lvDream.onRefreshComplete();
                        }
                    });
                } else {//加载下一页
                    initPullRefreshText();
                    actionType = STATE_MORE;
                    mPresenter.getDreamList(curPage, actionType);// 上拉加载更多(加载下一页数据)
                    lvDream.post(new Runnable() {
                        @Override
                        public void run() {
                            lvDream.onRefreshComplete();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected DreamPresenter createdPresenter() {
        return new DreamPresenter(mActivity);
    }

    @Override
    public void updateDreamList(List<DreamBean> list) {
        if (list != null && list.size() > 0) {
            rlEmptyView.setVisibility(View.GONE);
            lvDream.setVisibility(View.VISIBLE);

            hasNewData = list.size() == 10;
            if (actionType == STATE_REFRESH) {
                curPage = 1;
            } else if (actionType == STATE_MORE) {
                curPage++;
            }
            if (adapter == null) {
                adapter = new DreamListAdapter(mActivity);
                adapter.setItems(list);
                lvDream.setAdapter(adapter);
            } else {
                adapter.setItems(list);
            }
        } else {
            rlEmptyView.setVisibility(View.VISIBLE);
            lvDream.setVisibility(View.GONE);
        }
    }
}
