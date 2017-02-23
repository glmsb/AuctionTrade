package com.wyd.auctiontrade.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.adapter.DreamListAdapter;
import com.wyd.auctiontrade.bean.DreamBean;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.iView.IViewDream;
import com.wyd.auctiontrade.presenter.DreamPresenter;

import org.kymjs.kjframe.ui.BindView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

public class MyDreamActivity extends MVPBaseActivity<IViewDream, DreamPresenter> implements IViewDream {
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.lv_my_dream)
    private ListView lvMyDream;
    @BindView(id = R.id.rl_empty_view)
    private RelativeLayout rlEmptyView;

    private DreamListAdapter adapter;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_my_dream);
    }

    @Override
    public void initData() {
        super.initData();
        BmobQuery<DreamBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", BmobUser.getCurrentUser(this, UserInfoBean.class));
        query.order("-createdAt");
        mPresenter.getMyDreamList(query);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText("我的梦想");
    }

    @Override
    protected DreamPresenter createdPresenter() {
        return new DreamPresenter(this);
    }

    @Override
    public void updateDreamList(List<DreamBean> list) {
        if (list != null && list.size() > 0) {
            rlEmptyView.setVisibility(View.GONE);
            lvMyDream.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new DreamListAdapter(this);
                adapter.setItems(list);
                lvMyDream.setAdapter(adapter);
            } else {
                adapter.setItems(list);
            }
        } else {
            rlEmptyView.setVisibility(View.VISIBLE);
            lvMyDream.setVisibility(View.GONE);
        }
    }
}
