package com.wyd.auctiontrade.iView;

/**
 * Description:登录后（请求网络后）所需要更改UI的部分
 * Created by wyd on 2016/4/8.
 */
public interface IViewLogin {

    //跳转到主页
    void goToHome();

    //登录失败时相应的提示
    void prompt();
}
