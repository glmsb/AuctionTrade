package com.wyd.auctiontrade.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Description:presenter的父类，实现弱引用
 * Created by wyd on 2016/4/8.
 * V--View的接口类型,是Activity或者Fragment等去实现的某个特定接口的的类型。
 */
public abstract class BasePresenter<V> {
    protected Reference<V> mViewRef;  // View接口类型的 弱引用

    public void attachView(V IView) {
        mViewRef = new WeakReference<>(IView); //建立view与presenter间的关联
    }

    protected V getView() {
        return mViewRef.get();  //获取View
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear(); // 解除关联
            mViewRef = null;
        }
    }

    public abstract void initData();
}
