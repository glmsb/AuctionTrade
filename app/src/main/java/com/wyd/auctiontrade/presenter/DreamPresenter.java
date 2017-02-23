package com.wyd.auctiontrade.presenter;

import android.content.Context;

import com.wyd.auctiontrade.bean.DreamBean;
import com.wyd.auctiontrade.iView.IViewDream;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Description :
 * Created by wyd on 2016/5/28.
 */
public class DreamPresenter extends BasePresenter<IViewDream> {
    private Context mContext;
    private IViewDream iViewDream;

    public DreamPresenter(Context mContext) {
        this.mContext = mContext;
    }


    public void getDreamList(int page,int stateRefresh) {
        UiUtil.showRoundProcessDialog(mContext, true);
        BmobQuery<DreamBean> dreamBeanBmobQuery = new BmobQuery<>();
        dreamBeanBmobQuery.order("-createdAt");
        dreamBeanBmobQuery.include("userName");// 希望在拍品信息的同时也把发布人的信息查询出来
        if (stateRefresh == 1) {// 如果是加载更多
            dreamBeanBmobQuery.setSkip(page * 10);
        }
        dreamBeanBmobQuery.setLimit(10); // 限制最多10条数据结果作为一页
        dreamBeanBmobQuery.findObjects(mContext, new FindListener<DreamBean>() {
            @Override
            public void onSuccess(List<DreamBean> list) {
                LogUtil.e("getDreamList", "共" + list.size() + "条数据");
                iViewDream.updateDreamList(list);
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("getDreamList", i + s);
                UiUtil.showToast(mContext, "查询失败");
                UiUtil.dismissProcessDialog();
            }
        });
    }

    @Override
    public void initData() {
        this.iViewDream = getView();
    }

    public void getMyDreamList(BmobQuery<DreamBean> query) {
        UiUtil.showRoundProcessDialog(mContext, true);
        query.findObjects(mContext, new FindListener<DreamBean>() {
            @Override
            public void onSuccess(List<DreamBean> list) {
                LogUtil.e("getMyDreamList", list.toString());
                iViewDream.updateDreamList(list);
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("getMyDreamList", i + s);
                UiUtil.showToast(mContext, "查询失败");
                UiUtil.dismissProcessDialog();
            }
        });
    }
}

