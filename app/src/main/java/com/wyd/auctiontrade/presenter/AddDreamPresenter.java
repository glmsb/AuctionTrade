package com.wyd.auctiontrade.presenter;


import android.content.Context;

import com.wyd.auctiontrade.bean.DreamBean;
import com.wyd.auctiontrade.iView.IViewAddDream;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import cn.bmob.v3.listener.SaveListener;

/**
 * Description :
 * Created by wyd on 2016/5/16.
 */
public class AddDreamPresenter extends BasePresenter<IViewAddDream> {
    private Context mContext;
    private IViewAddDream iViewAddDream;

    public AddDreamPresenter(Context mContext) {
        this.mContext = mContext;
    }


    public void addDream(DreamBean reqParams) {
        /**
         *  向服务器添加数据
         */
        UiUtil.showRoundProcessDialog(mContext, true);
        reqParams.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.e("addDream", "发布成功！！");
                UiUtil.showToast(mContext, "发布成功！！");
                iViewAddDream = getView();
                iViewAddDream.returnDreamList();
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e("addDream", "发布失败" + s);
                UiUtil.showToast(mContext, "发布失败" + s);
                UiUtil.dismissProcessDialog();
            }
        });
    }

    @Override
    public void initData() {
        iViewAddDream = getView();
    }
}
