package com.wyd.auctiontrade.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.wyd.auctiontrade.presenter.BasePresenter;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.utils.SystemTool;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Description:两个泛型类：V--View的接口类型；T--Presenter的具体类型
 * {通过Activity、Fragment的生命周期来控制它与Presenter的关系}
 * Created by wyd on 2016/4/8.
 */
public abstract class MVPBaseActivity<V, T extends BasePresenter<V>> extends KJActivity {
    protected T mPresenter;  //Presenter对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //这句代码一定要在 setContentView()之前执行，不然会报错
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (!SystemTool.checkNet(this)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("提示");
            dialog.setMessage("当前网络状况不佳，请检查网络");
            dialog.setCancelable(false);
            dialog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        //提供以下两种方式进行初始化操作：
        //第一：默认初始化
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        // Bmob.initialize(this, "0ce091604ffc39fc2ea2ca98f568fc32");

        //第二：设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，自v3.4.7版本开始
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("0ce091604ffc39fc2ea2ca98f568fc32")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);

        mPresenter = createdPresenter();
        mPresenter.attachView((V) this);
        //将当前正在创建的活动添加到活动管理器里
        ActivityCollector.addActivity(this);
    }

    @Override
    public void initData() {
        super.initData();
        mPresenter = createdPresenter();
        mPresenter.attachView((V) this);
        mPresenter.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        //将一个马上要销毁的活动从活动管理器里移除
        ActivityCollector.removeActivity(this);
    }

    /**
     * 获取资源方法，适应不同版本
     */
    protected Drawable getDrawableById(int id) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getDrawable(id);
        } else {
            return getResources().getDrawable(id);
        }
    }

    protected int getColorById(int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getColor(id);
        } else {
            return getResources().getColor(id);
        }
    }


    protected abstract T createdPresenter(); //由实现了该类的子类去创建presenter对象
}
