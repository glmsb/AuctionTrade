package com.wyd.auctiontrade.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.iView.IViewLogin;
import com.wyd.auctiontrade.presenter.LoginPresenter;
import com.wyd.auctiontrade.util.UiUtil;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.CipherUtils;
import org.kymjs.kjframe.utils.StringUtils;

public class LoginActivity extends MVPBaseActivity<IViewLogin, LoginPresenter> implements IViewLogin {

    @BindView(id = R.id.et_username)
    private EditText etUserName;
    @BindView(id = R.id.et_password)
    private EditText etPassword;
    @BindView(id = R.id.btn_login, click = true)
    private Button btnLogin;
    @BindView(id = R.id.btn_forget_pass, click = true)
    private Button btnForgetPass;
    @BindView(id = R.id.tv_register_link, click = true)
    private TextView tvRegister;

    private Context mContext;
    private String account;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_login);
    }

    @Override
    public void initData() {
        super.initData();
        mContext = LoginActivity.this;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        // TODO: 2016/5/15  开发时使用
//        etUserName.setText("18600000001");
//        etPassword.setText("123456");
//        etUserName.setSelection(etUserName.getText().length());
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                getAccountAndPassword();//获取用户名和密码，并请求网络
                break;
            case R.id.btn_forget_pass:
                Intent intentResetPass = new Intent(mContext, ForgetPassActivity.class);
                account = etUserName.getText().toString();
                intentResetPass.putExtra("mobile",account);
                startActivity(intentResetPass);
                break;
            case R.id.tv_register_link:
                Intent intent = new Intent(mContext, RegisterActivity.class);
                account = etUserName.getText().toString();
                intent.putExtra("mobile",account);
                startActivity(intent);
                break;
        }
    }

    private void getAccountAndPassword() {
        account = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if (StringUtils.isEmpty(account)) {
            UiUtil.showToast(mContext, R.string.pmt_account_empty);
            UiUtil.popSoftKeyboard(etUserName);
        } else if (!UiUtil.isPhone(account)) {
            UiUtil.showToast(this, getString(R.string.pmt_check_account));
            UiUtil.popSoftKeyboard(etUserName);
        } else if (StringUtils.isEmpty(password)) {
            UiUtil.showToast(mContext, R.string.pmt_password_empty);
            UiUtil.popSoftKeyboard(etPassword);
        } else if (password.length() < 6 || password.length() > 16) {
            UiUtil.showToast(this, getString(R.string.pmt_input_confirm_pw));
            UiUtil.popSoftKeyboard(etPassword);
        } else {
            String encryptionPW = CipherUtils.md5(password);
            mPresenter.login(account, encryptionPW);
        }
    }


    @Override
    protected LoginPresenter createdPresenter() {
        mPresenter = new LoginPresenter(this);
        return mPresenter;
    }

    @Override
    public void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }

    //需要去注册吗
    @Override
    public void prompt() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("提示");
        dialog.setMessage("当前手机号还没有注册过，现在去注册吗？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(mContext, RegisterActivity.class));
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
