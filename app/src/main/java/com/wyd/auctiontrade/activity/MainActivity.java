package com.wyd.auctiontrade.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.adapter.MyFragmentPagerAdapter;
import com.wyd.auctiontrade.fragment.AuctionFragment;
import com.wyd.auctiontrade.fragment.DreamFragment;
import com.wyd.auctiontrade.fragment.PersonalFragment;
import com.wyd.auctiontrade.presenter.BasePresenter;
import com.wyd.auctiontrade.util.LogUtil;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MVPBaseActivity {
    private static final int REQUEST_CODE_ADD_AUCTION = 1;
    private static final int REQUEST_CODE_ADD_DREAM = 2;

    @BindView(id = R.id.imv_back)
    private ImageView imvBack;
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.tv_click_action, click = true)
    private TextView tvClickAction;
    @BindView(id = R.id.view_pager)
    private ViewPager viewPager;
    @BindView(id = R.id.rb_auction, click = true)
    private RadioButton rbAuction;
    @BindView(id = R.id.rb_dream, click = true)
    private RadioButton rbDream;
    @BindView(id = R.id.rb_personal, click = true)
    private RadioButton rbPersonal;

    private int currentItem;
    private AuctionFragment auctionFragment;
    private DreamFragment dreamFragment;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_main);
    }

    @Override
    public void initData() {
        super.initData();
        currentItem = 0;
        List<Fragment> fragments = new ArrayList<>();
        auctionFragment = new AuctionFragment();
        dreamFragment = new DreamFragment();
        PersonalFragment personalFragment = new PersonalFragment();
        fragments.add(auctionFragment);
        fragments.add(dreamFragment);
        fragments.add(personalFragment);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        imvBack.setVisibility(View.GONE);
        tvClickAction.setVisibility(View.VISIBLE);
        changeTopTitleBar(currentItem);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            changeTopTitleBar(currentItem);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.rb_auction:
                currentItem = 0;
                changeTopTitleBar(currentItem);
                break;
            case R.id.rb_dream:
                currentItem = 1;
                changeTopTitleBar(currentItem);
                break;
            case R.id.rb_personal:
                currentItem = 2;
                changeTopTitleBar(currentItem);
                break;
            case R.id.tv_click_action:
                switch (currentItem) {
                    case 0:
                        startActivityForResult(new Intent(this, AddAuctionActivity.class), REQUEST_CODE_ADD_AUCTION);
                        break;
                    case 1:
                        startActivityForResult(new Intent(this, AddDreamActivity.class), REQUEST_CODE_ADD_DREAM);
                        break;
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_AUCTION && resultCode == AddAuctionActivity.RESULT_CODE_AUCTION_LIST){
            changeTopTitleBar(0);
            auctionFragment.initData();
        }else if (requestCode == REQUEST_CODE_ADD_DREAM && resultCode == AddDreamActivity.RESULT_CODE_DREAM_LIST){
            changeTopTitleBar(1);
            dreamFragment.initData();
        }
    }

    /**
     * 根据当前页面改变顶部对应的标题栏
     *
     * @param currentItem 当前页面
     */
    private void changeTopTitleBar(int currentItem) {
        LogUtil.e("currentItem", currentItem + "");
        viewPager.setCurrentItem(currentItem);
        switch (currentItem) {
            case 0:
                rbAuction.setChecked(true);
                tvTitle.setText("拍卖中");
                tvClickAction.setText("我想卖");
                break;
            case 1:
                rbDream.setChecked(true);
                tvTitle.setText("在线等");
                tvClickAction.setText("我想要");
                break;
            case 2:
                rbPersonal.setChecked(true);
                tvTitle.setText("我的小窝");
                tvClickAction.setText("");
                break;
        }
    }
}
