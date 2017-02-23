package com.wyd.auctiontrade.presenter;

import android.content.Context;
import android.util.Log;

import com.wyd.auctiontrade.iView.IViewLogin;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

/**
 * Description:登录的逻辑
 * Created by wyd on 2016/4/8.
 */
public class LoginPresenter extends BasePresenter<IViewLogin> {
    private Context mContext;
    private IViewLogin iViewLogin;

    public LoginPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void initData() {
        this.iViewLogin = getView();
    }

    public void login(final String account, String password) {
        UiUtil.showRoundProcessDialog(mContext, true);
        /**
         * 通过手机号码和密码登陆
         */
        BmobUser.loginByAccount(mContext, account, password, new LogInListener<UserInfoBean>() {
            @Override
            public void done(UserInfoBean user, BmobException e) {
                if (user != null) {
                    LogUtil.e("smile", "" + user.getUsername() + "-" + user.getAge() + "-" + user.getObjectId() + "-" + user.getEmail());
                    iViewLogin = getView();
                    iViewLogin.goToHome();
                } else {
                    Log.e("login", "错误码：" + e.getErrorCode() + ",错误原因：" + e.getLocalizedMessage());
                    searchAccount(account);
                }
                UiUtil.dismissProcessDialog();
            }
        });
    }

    private void searchAccount(String account) {
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber", account);
        query.findObjects(mContext, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> object) {
                if (object.size() > 0) {
                    UiUtil.showToast(mContext, "用户名或者密码错误,请重新输入");
                } else {
                    iViewLogin = getView();
                    iViewLogin.prompt();
                }
            }

            @Override
            public void onError(int code, String msg) {
                UiUtil.showToast(mContext, "查询用户失败：" + msg);
            }
        });
    }
}
