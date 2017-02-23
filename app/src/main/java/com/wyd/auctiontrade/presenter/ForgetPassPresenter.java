package com.wyd.auctiontrade.presenter;

import android.content.Context;

import com.wyd.auctiontrade.iView.IViewForgetPass;
import com.wyd.auctiontrade.util.LogUtil;
import com.wyd.auctiontrade.util.UiUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

/**
 * Description :
 * Created by wyd on 2016/5/29.
 */
public class ForgetPassPresenter extends BasePresenter<IViewForgetPass> {
    private Context mContext;
    private IViewForgetPass iViewForgetPass;
    private boolean mobileIsRegister = false;

    public ForgetPassPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void initData() {
        iViewForgetPass = getView();
    }


    public void resetPasswordByEmail(final String email) {
        UiUtil.showRoundProcessDialog(mContext, true);
        BmobUser.resetPasswordByEmail(mContext, email, new ResetPasswordByEmailListener() {
            @Override
            public void onSuccess() {
                iViewForgetPass = getView();
                //iViewForgetPass.refreshUI();
                LogUtil.e("resetPasswordByEmail", "重置密码成功" + email);
                UiUtil.dismissProcessDialog();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.e("resetPasswordByEmail", "重置密码失败" + s);
                UiUtil.showToast(mContext, "重置密码失败" + s);
                UiUtil.dismissProcessDialog();
            }
        });
    }

    public void sentSMS(String mobilePhoneNumber, String auctionsms) {
        if (searchAccount(mobilePhoneNumber)) {
            BmobSMS.requestSMSCode(mContext, mobilePhoneNumber, auctionsms, new RequestSMSCodeListener() {
                @Override
                public void done(Integer smsId, BmobException ex) {
                    if (ex == null) {
                        LogUtil.e("bmob", "短信发送成功，短信id：" + smsId);//用于查询本次短信发送详情
                        iViewForgetPass = getView();
                        iViewForgetPass.changeButtonText();
                    } else {
                        UiUtil.showToast(mContext, "获取验证码发送失败:" + ex.getMessage());
                        LogUtil.e("bmob", "errorCode = " + ex.getErrorCode() + ",errorMsg = " + ex.getLocalizedMessage());
                    }
                }
            });
        }
    }

    private boolean searchAccount(String account) {
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber", account);
        query.findObjects(mContext, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> object) {
                if (object.size() > 0) {
                    mobileIsRegister = true;
                } else {
                    UiUtil.showToast(mContext, "该手机号还没有注册过，暂时不能获取验证码哦");
                }
            }

            @Override
            public void onError(int code, String msg) {
                UiUtil.showToast(mContext, "网络状况不佳：" + msg);
            }
        });
        return mobileIsRegister;
    }

    public void resetPasswordBySMSCode(String emsCode, String pw) {
        UiUtil.showRoundProcessDialog(mContext, true);
        BmobUser.resetPasswordBySMSCode(mContext, emsCode, pw, new ResetPasswordByCodeListener() {
            @Override
            public void done(BmobException ex) {
                if (ex == null) {
                    LogUtil.e("resetPasswordBySMSCode", "密码重置成功");
                    UiUtil.showToast(mContext, "密码重置成功");
                    iViewForgetPass = getView();
                    iViewForgetPass.jumpToLogin();
                } else {
                    LogUtil.e("resetPasswordBySMSCode", "重置失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    UiUtil.showToast(mContext, "密码重置失败:" + ex.getMessage());
                }
                UiUtil.dismissProcessDialog();
            }
        });
    }
}
