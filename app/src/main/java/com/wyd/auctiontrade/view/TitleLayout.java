package com.wyd.auctiontrade.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.activity.ActivityCollector;

/**
 * Description:自定义的标题栏控件
 * Created by wyd on 2016/4/14.
 */
public class TitleLayout extends RelativeLayout {

    private Context mContext;
    private TextView tvTitle;
    private TextView tvRight;

    public TitleLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        RelativeLayout rlTitleLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.v_top_title_bar, this);
        ImageView imvBack = (ImageView) rlTitleLayout.findViewById(R.id.imv_back);
        tvTitle = (TextView) rlTitleLayout.findViewById(R.id.tv_title);
        tvRight = (TextView) rlTitleLayout.findViewById(R.id.tv_click_action);
        imvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.removeActivity((Activity) mContext);
            }
        });
    }

    public void setTvRight(String text) {
        this.tvRight.setText(text);
    }

    public void setTvTitle(String text) {
        this.tvTitle.setText(text);
    }

    public void setRightListener(OnClickListener onClickListener) {
        tvRight.setOnClickListener(onClickListener);
    }
}
