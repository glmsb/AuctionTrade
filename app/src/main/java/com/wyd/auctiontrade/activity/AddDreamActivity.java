package com.wyd.auctiontrade.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.bean.DreamBean;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.iView.IViewAddDream;
import com.wyd.auctiontrade.presenter.AddDreamPresenter;
import com.wyd.auctiontrade.util.UiUtil;
import com.wyd.auctiontrade.view.PickerView;
import com.wyd.auctiontrade.view.PickerViewData;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class AddDreamActivity extends MVPBaseActivity<IViewAddDream, AddDreamPresenter> implements IViewAddDream {
    public static final int RESULT_CODE_DREAM_LIST = 1;

    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.tv_click_action, click = true)
    private TextView tvClickAction;
    @BindView(id = R.id.et_dream_name)
    private EditText etDreamName;
    @BindView(id = R.id.et_dream_description)
    private EditText etDreamDesc;
    @BindView(id = R.id.tv_dream_type, click = true)
    private TextView tvDreamType;
    @BindView(id = R.id.et_dream_type)
    private EditText etDreamType;

    private Context mContext;
    private PopupWindow popupWindow;
    private String[] itemsDreamType;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_add_dream);
    }

    @Override
    public void initData() {
        super.initData();
        mContext = AddDreamActivity.this;
        itemsDreamType = getResources().getStringArray(R.array.dream_type);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText("提出梦想");
        tvClickAction.setText("提出");
        tvClickAction.setVisibility(View.VISIBLE);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.tv_click_action) {
            judgeAndSubmit();
        } else if (v.getId() == R.id.tv_dream_type) {
            triggerPopupWindow();
        }
    }

    private void triggerPopupWindow() {
        if (popupWindow == null) {
            List<PickerViewData> dataList = new ArrayList<>();
            for (String anItemsDreamType : itemsDreamType) {
                PickerViewData pickerViewData = new PickerViewData();
                pickerViewData.setText(anItemsDreamType);
                dataList.add(pickerViewData);
            }
            RelativeLayout rlView = (RelativeLayout) getLayoutInflater().inflate(R.layout.v_picker_window_choose, null);
            RelativeLayout parent = (RelativeLayout) rlView.findViewById(R.id.parent);
            PickerView pickerView = (PickerView) rlView.findViewById(R.id.picker_view);
            pickerView.setData(dataList);
            pickerView.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(PickerViewData data) {
                    tvDreamType.setText(data.getText());
                    if (tvDreamType.getText().equals("其他")) {
                        etDreamType.setVisibility(View.VISIBLE);
                    } else {
                        etDreamType.setVisibility(View.GONE);
                    }
                }
            });
            popupWindow = new PopupWindow(rlView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//背景不为空但是完全透明
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(etDreamName, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
    }

    private void judgeAndSubmit() {
        String dreamName = etDreamName.getText().toString();
        String dreamDesc = etDreamDesc.getText().toString();
        String dreamType = tvDreamType.getText().toString();
        String OtherDreamType = etDreamType.getText().toString();
        if (StringUtils.isEmpty(dreamName)) {
            UiUtil.showToast(mContext, "亲，你必须要告诉我你的梦想宝贝是什么哦(⊙o⊙)…");
            UiUtil.popSoftKeyboard(etDreamName);
        } else if (StringUtils.isEmpty(dreamType)) {
            UiUtil.showToast(mContext, "亲，你还没有选择类型哦(⊙o⊙)…");
        } else if (dreamType.equals("其他") && StringUtils.isEmpty(OtherDreamType)) {
            UiUtil.showToast(mContext, "亲，你还没有填写类型哦(⊙o⊙)…");
            UiUtil.popSoftKeyboard(etDreamType);
        } else {
            DreamBean reqParams = new DreamBean();
            reqParams.setName(dreamName);
            reqParams.setDescription(dreamDesc);
            reqParams.setType(dreamType.equals("其他") ? OtherDreamType : dreamType);
            UserInfoBean user = BmobUser.getCurrentUser(mContext, UserInfoBean.class);
            reqParams.setUserName(user);
            mPresenter.addDream(reqParams);
        }
    }

    @Override
    protected AddDreamPresenter createdPresenter() {
        return new AddDreamPresenter(this);
    }

    @Override
    public void returnDreamList() {
        setResult(RESULT_CODE_DREAM_LIST);
        this.finish();
    }
}
