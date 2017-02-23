package com.wyd.auctiontrade.presenter;

import android.content.Context;

import com.wyd.auctiontrade.iView.IViewRegister;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import cn.bmob.v3.listener.SaveListener;

/**
 * Description:注册
 * Created by wyd on 2016/4/24.
 */
public class RegisterPresenter extends BasePresenter<IViewRegister> {
    private Context mContext;
    private IViewRegister iViewRegister;

    public RegisterPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void register(String phone, String passWord, String email, String userName, String sex) {
        UiUtil.showRoundProcessDialog(mContext, true);

        final UserInfoBean userInfo = new UserInfoBean();
        userInfo.setMobilePhoneNumber(phone);
        userInfo.setPassword(passWord);
        userInfo.setEmail(email);
        userInfo.setUsername(userName);
        userInfo.setSex(sex);


        /**
         * 注册用户
         */
       /* 在注册过程中，服务器会对注册用户信息进行检查，以确保注册的用户名和电子邮件地址是独一无二的。*/
        userInfo.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.e("signUp", "注册成功！" + userInfo.getUsername() + "-"
                        + userInfo.getObjectId() + "-" + userInfo.getCreatedAt()
                        + "-" + userInfo.getSessionToken() + ",是否验证：" + userInfo.getEmailVerified());
                iViewRegister = getView();
                iViewRegister.goToHome();
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e("signUp", "注册失败" + s);
                UiUtil.showToast(mContext, "注册失败" + s);
                UiUtil.dismissProcessDialog();
            }
        });

    }

    @Override
    public void initData() {
        this.iViewRegister = getView();
    }
}
