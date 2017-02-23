package com.wyd.auctiontrade.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.activity.AboutActivity;
import com.wyd.auctiontrade.activity.ActivityCollector;
import com.wyd.auctiontrade.activity.LoginActivity;
import com.wyd.auctiontrade.activity.MyAuctionActivity;
import com.wyd.auctiontrade.activity.MyDreamActivity;
import com.wyd.auctiontrade.activity.ViewAccountInfoActivity;

import cn.bmob.v3.BmobUser;

/**
 * Description :个人中心
 * Created by wyd on 2016/5/10.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_personal, container, false);
            initData();
        } else {
            ViewGroup p = (ViewGroup) rootView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }
        return rootView;
    }

    private void initData() {
        TextView tvAccountInfo = (TextView) rootView.findViewById(R.id.tv_account_info);
        TextView tvMyAuction = (TextView) rootView.findViewById(R.id.tv_my_auction);
        TextView tvMyDream = (TextView) rootView.findViewById(R.id.tv_my_dream);
        TextView tvAbout = (TextView) rootView.findViewById(R.id.tv_about);
        TextView tvExitLogin = (TextView) rootView.findViewById(R.id.tv_exit_login);
        tvAccountInfo.setOnClickListener(this);
        tvMyAuction.setOnClickListener(this);
        tvMyDream.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        tvExitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_account_info:
                startActivity(new Intent(getContext(), ViewAccountInfoActivity.class));
                break;
            case R.id.tv_my_auction:
                startActivity(new Intent(getContext(), MyAuctionActivity.class));
                break;
            case R.id.tv_my_dream:
                startActivity(new Intent(getContext(), MyDreamActivity.class));
                break;
            case R.id.tv_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.tv_exit_login:
                BmobUser.logOut(getContext());   //清除缓存用户对象
                startActivity(new Intent(getContext(), LoginActivity.class));
                ActivityCollector.finishAll();
                break;
        }
    }
}
