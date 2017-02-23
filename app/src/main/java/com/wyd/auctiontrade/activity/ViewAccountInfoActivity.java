package com.wyd.auctiontrade.activity;

import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.presenter.BasePresenter;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.SystemTool;

import cn.bmob.v3.BmobUser;

public class ViewAccountInfoActivity extends MVPBaseActivity {

    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.tv_user_name)
    private TextView tvUserName;
    @BindView(id = R.id.tv_sex)
    private TextView tvSex;
    @BindView(id = R.id.tv_mobile)
    private TextView tvMobile;
    @BindView(id = R.id.tv_email)
    private TextView tvEmail;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_view_account_info);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        UserInfoBean user = BmobUser.getCurrentUser(this, UserInfoBean.class);
        tvTitle.setText("账户详情");
        tvUserName.setText(user.getUsername());
        tvMobile.setText(user.getMobilePhoneNumber());
        tvEmail.setText(user.getEmail());
        if (user.getSex().equals("0")) {
            tvSex.setText("男");
        } else if (user.getSex().equals("1")) {
            tvSex.setText("女");
        }
    }

    @Override
    protected BasePresenter createdPresenter() {
        return new BasePresenter() {
            @Override
            public void initData() {
            }
        };
    }
}
