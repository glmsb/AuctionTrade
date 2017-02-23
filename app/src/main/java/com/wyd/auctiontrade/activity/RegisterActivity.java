package com.wyd.auctiontrade.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wyd.auctiontrade.iView.IViewRegister;
import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.presenter.LoginPresenter;
import com.wyd.auctiontrade.presenter.RegisterPresenter;
import com.wyd.auctiontrade.util.UiUtil;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.CipherUtils;
import org.kymjs.kjframe.utils.StringUtils;

public class RegisterActivity extends MVPBaseActivity<IViewRegister, RegisterPresenter> implements IViewRegister {
    @BindView(id = R.id.et_phone)
    private EditText etPhone;
    @BindView(id = R.id.et_password)
    private EditText etPassword;
    @BindView(id = R.id.et_sure_password)
    private EditText etSurePassword;
    @BindView(id = R.id.et_email)
    private EditText etEmail;
    @BindView(id = R.id.et_user_name)
    private EditText etUserName;
    @BindView(id = R.id.rb_man)
    private RadioButton rbMan;
    @BindView(id = R.id.rb_women)
    private RadioButton rbWomen;
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.tv_click_action, click = true)
    private TextView tvClickAction;

    private Context mContext;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_register);
    }

    @Override
    public void initData() {
        super.initData();
        mContext = RegisterActivity.this;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText("注册");
        tvClickAction.setText("下一步");
        tvClickAction.setVisibility(View.VISIBLE);
        tvClickAction.setTextColor(getColorById(R.color.c_major_assist));
        etPhone.setText(getIntent().getStringExtra("mobile"));
        etPhone.setSelection(etPhone.getText().length());
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.tv_click_action) {
            judgeAndSubmit();
        }
    }

    private void judgeAndSubmit() {
        String phone = etPhone.getText().toString();
        String passWord = etPassword.getText().toString();
        String surePassword = etSurePassword.getText().toString();
        String email = etEmail.getText().toString();
        String userName = etUserName.getText().toString();
        String sex = "";
        if (rbMan.isChecked()) {
            sex = "0";
        } else if (rbWomen.isChecked()) {
            sex = "1";
        }
        if (StringUtils.isEmpty(phone)) {
            UiUtil.showToast(mContext, R.string.pmt_account_empty);
            UiUtil.popSoftKeyboard(etPhone);
        } else if (!UiUtil.isPhone(phone)) {
            UiUtil.showToast(this, getString(R.string.pmt_check_account));
            UiUtil.popSoftKeyboard(etPhone);
        } else if (StringUtils.isEmpty(passWord)) {
            UiUtil.showToast(mContext, R.string.pmt_password_empty);
            UiUtil.popSoftKeyboard(etPassword);
        } else if (passWord.length() < 6 || passWord.length() > 16) {
            UiUtil.showToast(this, getString(R.string.pmt_input_confirm_pw));
            UiUtil.popSoftKeyboard(etPassword);
        } else if (StringUtils.isEmpty(surePassword)) {
            UiUtil.showToast(mContext, R.string.pmt_sure_password_empty);
            UiUtil.popSoftKeyboard(etSurePassword);
        } else if (!surePassword.equals(passWord)) {
            UiUtil.showToast(mContext, R.string.pmt_password_error);
            UiUtil.popSoftKeyboard(etSurePassword);
        } else if (StringUtils.isEmpty(email)) {
            UiUtil.showToast(mContext, R.string.pmt_email_empty);
            UiUtil.popSoftKeyboard(etEmail);
        } else if (!StringUtils.isEmail(email)) {
            UiUtil.showToast(mContext, R.string.pmt_email_error);
            UiUtil.popSoftKeyboard(etEmail);
        } else if (StringUtils.isEmpty(userName)) {
            UiUtil.showToast(mContext, R.string.pmt_name_empty);
            UiUtil.popSoftKeyboard(etUserName);
        } else if (StringUtils.isEmpty(sex)) {
            UiUtil.showToast(mContext, R.string.pmt_sex_empty);
        } else {
            tvClickAction.setTextColor(getColorById(R.color.red));
            String encryptionPW = CipherUtils.md5(passWord);
            mPresenter.register(phone, encryptionPW, email, userName, sex);
        }
    }

    @Override
    protected RegisterPresenter createdPresenter() {
        mPresenter = new RegisterPresenter(this);
        return mPresenter;
    }

    @Override
    public void goToHome() {
        startActivity(new Intent(mContext, MainActivity.class));
    }
}
