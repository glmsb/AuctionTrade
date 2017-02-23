package com.wyd.auctiontrade.activity;

import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.presenter.BasePresenter;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.SystemTool;

public class AboutActivity extends MVPBaseActivity {
    @BindView(id = R.id.tv_version)
    private TextView tvVersion;
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;

    @Override
    protected BasePresenter createdPresenter() {
        return new BasePresenter() {
            @Override
            public void initData() {
            }
        };
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_about);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvVersion.setText(SystemTool.getAppVersionName(this));
        tvTitle.setText("关于");
    }
}
