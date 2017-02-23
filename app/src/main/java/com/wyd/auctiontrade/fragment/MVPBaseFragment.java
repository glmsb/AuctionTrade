package com.wyd.auctiontrade.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wyd.auctiontrade.presenter.BasePresenter;


/**
 * MVPBaseFragment基类{通过基类的声明周期函数来控制它与Presenter的关系}
 * Description:两个泛型类：V-View的接口类型；T-Presenter的具体类型
 * Created by wyd on 2016/4/8.
 */
public abstract class MVPBaseFragment<V, T extends BasePresenter<V>> extends Fragment {
	protected T mPresenter;  // Presenter对象
	protected Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mPresenter = createdPresenter();
		mPresenter.attachView((V) this);
		mPresenter.initData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mPresenter != null) {
			mPresenter.detachView();
		}
	}

	protected Drawable getDrawableById(int id) {
		if (Build.VERSION.SDK_INT >= 21) {
			return getResources().getDrawable(id, mActivity.getTheme());
		} else {
			return getResources().getDrawable(id);
		}
	}

	protected int getColorById(int id) {
		if (Build.VERSION.SDK_INT >= 23) {
			return getResources().getColor(id, mActivity.getTheme());
		} else {
			return getResources().getColor(id);
		}
	}


	protected abstract T createdPresenter(); //由实现了该类的子类去创建presenter对象
}
