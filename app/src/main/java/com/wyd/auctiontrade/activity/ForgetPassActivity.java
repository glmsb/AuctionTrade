package com.wyd.auctiontrade.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.internal.Utils;
import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.iView.IViewForgetPass;
import com.wyd.auctiontrade.presenter.ForgetPassPresenter;
import com.wyd.auctiontrade.util.UiUtil;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.CipherUtils;
import org.kymjs.kjframe.utils.StringUtils;

public class ForgetPassActivity extends MVPBaseActivity<IViewForgetPass, ForgetPassPresenter> implements IViewForgetPass {
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.tv_click_action, click = true)
    private TextView tvClickAction;
    @BindView(id = R.id.et_mobile)
    private EditText etMobile;
    @BindView(id = R.id.btn_get_sms_code, click = true)
    private Button btnGetSmsCode;
    @BindView(id = R.id.et_ems_code)
    private EditText etEmsCode;
    @BindView(id = R.id.et_new_password)
    private EditText etNewPassword;


    @Override
    public void setRootView() {
        setContentView(R.layout.aty_forget_pass);
    }


    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText("忘记密码");
        tvClickAction.setText("重置");
        tvClickAction.setVisibility(View.VISIBLE);
        etMobile.setText(getIntent().getStringExtra("mobile"));
        etMobile.setSelection(etMobile.getText().length());
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.tv_click_action) {
            judgeAndSubmit();
        }
        if (v.getId() == R.id.btn_get_sms_code) {//获取验证码
            String mobile = etMobile.getText().toString();
            if (StringUtils.isEmpty(mobile)) {
                UiUtil.showToast(this, R.string.hint_login_username);
                UiUtil.popSoftKeyboard(etMobile);
            } else if (!UiUtil.isPhone(mobile)) {
                UiUtil.showToast(this, R.string.pmt_check_account);
                UiUtil.popSoftKeyboard(etMobile);
            } else {
                mPresenter.sentSMS(mobile, "smsCode");
            }
        }
    }

    private void judgeAndSubmit() {//根据验证码重置密码
        String mobile = etMobile.getText().toString();
        String emsCode = etEmsCode.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        if (StringUtils.isEmpty(mobile)) {
            UiUtil.showToast(this, R.string.hint_login_username);
            UiUtil.popSoftKeyboard(etMobile);
        } else if (!UiUtil.isPhone(mobile)) {
            UiUtil.showToast(this, R.string.pmt_check_account);
            UiUtil.popSoftKeyboard(etMobile);
        } else if (StringUtils.isEmpty(emsCode)) {
            UiUtil.showToast(this, "请先填写验证码再重置");
            UiUtil.popSoftKeyboard(etEmsCode);
        } else if (StringUtils.isEmpty(newPassword)) {
            UiUtil.showToast(this, "请先填写新密码再重置");
            UiUtil.popSoftKeyboard(etNewPassword);
        } else {
            String encryptionPW = CipherUtils.md5(newPassword);
            mPresenter.resetPasswordBySMSCode(emsCode, encryptionPW);
        }
    }

    @Override
    protected ForgetPassPresenter createdPresenter() {
        return new ForgetPassPresenter(this);
    }

    @Override
    public void changeButtonText() {
        btnGetSmsCode.setText("已发送");
        btnGetSmsCode.setEnabled(false);
        etNewPassword.setVisibility(View.VISIBLE);
        UiUtil.popSoftKeyboard(etEmsCode);
    }

    @Override
    public void jumpToLogin() {
        this.finish();
    }

   /* @Override
    public void refreshUI() {
        tvPmtCheck.setVisibility(View.VISIBLE);
        tvPmtCheck.setText(String.format(getString(R.string.text_email_check), email));
    }*/
}
