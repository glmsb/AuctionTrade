package com.wyd.auctiontrade.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.adapter.SelectPicResultAdapter;
import com.wyd.auctiontrade.bean.ProductBean;
import com.wyd.auctiontrade.bean.UserInfoBean;
import com.wyd.auctiontrade.fragment.AuctionFragment;
import com.wyd.auctiontrade.iView.IVIewAddAuction;
import com.wyd.auctiontrade.presenter.AddAuctionPresenter;
import com.wyd.auctiontrade.util.UiUtil;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class AddAuctionActivity extends MVPBaseActivity<IVIewAddAuction, AddAuctionPresenter> implements IVIewAddAuction {
    public static final int RESULT_CODE_AUCTION_LIST = 1;
    private static final int REQUEST_CODE_SELECT_PHOTO = 2;
    private static final int REQUEST_CODE_PREVIEW_PHOTO = 3;

    @BindView(id = R.id.ll_root)
    private LinearLayout llRoot;
    @BindView(id = R.id.tv_title)
    private TextView tvTitle;
    @BindView(id = R.id.tv_click_action, click = true)
    private TextView tvClickAction;
    @BindView(id = R.id.et_auction_name)
    private EditText etAuctionName;
    @BindView(id = R.id.et_auction_price)
    private EditText etAuctionPrice;
    @BindView(id = R.id.et_auction_description)
    private EditText etAuctionDescription;
    @BindView(id = R.id.tbtn_auction_time)
    private ToggleButton toggleButton;
    @BindView(id = R.id.tv_auction_time, click = true)
    private TextView tvAuctionTime;
    @BindView(id = R.id.rl_add_photo, click = true)
    private RelativeLayout rlAddPhoto;
    @BindView(id = R.id.gv_photo_selected)
    private GridView gvPhotoSelected;
    @BindView(id = R.id.iv_photo)
    private ImageView ivPhoto;

    private Context mContext;
    private List<String> selectedPhotos;
    private SelectPicResultAdapter adapter;
    private PopupWindow popupWindow;
    private String[] itemsAuctionTimeText;
    private int[] itemsAuctionTimeNumber;
    private ProductBean reqParams;

    @Override
    public void setRootView() {
        setContentView(R.layout.aty_add_auction);
    }

    @Override
    public void initData() {
        super.initData();
        mContext = AddAuctionActivity.this;
        selectedPhotos = new ArrayList<>();
        selectedPhotos.add("addPic");
        reqParams = new ProductBean();
        itemsAuctionTimeText = getResources().getStringArray(R.array.auction_times_text);
        itemsAuctionTimeNumber = getResources().getIntArray(R.array.auction_times_number);
        reqParams.setTime(itemsAuctionTimeNumber[3]);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvTitle.setText("发布拍品");
        tvClickAction.setText("发布");
        tvClickAction.setVisibility(View.VISIBLE);
        if (!StringUtils.isEmpty(getIntent().getStringExtra("dreamName"))) {
            etAuctionName.setText(getIntent().getStringExtra("dreamName"));
            etAuctionDescription.setText(getIntent().getStringExtra("dreamDescription"));
        }
        toggleButton.setOnCheckedChangeListener(onCheckedChangeListener);
        gvPhotoSelected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItemId(position) == selectedPhotos.size() - 1) {
                    PhotoPickerIntent intent = new PhotoPickerIntent(mContext);
                    intent.setPhotoCount(9);
                    intent.setShowCamera(true);
                    intent.setShowGif(true);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
                } else {
                    Intent intent = new Intent(mContext, PhotoPagerActivity.class);
                    intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
                    selectedPhotos.remove("addPic");
                    intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, (Serializable) selectedPhotos);
                    intent.putExtra(PhotoPagerActivity.EXTRA_SHOW_DELETE, true);
                    startActivityForResult(intent, REQUEST_CODE_PREVIEW_PHOTO);
                }
            }
        });
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.tv_click_action:
                judgeAndSubmit();
                break;
            case R.id.rl_add_photo:
                PhotoPickerIntent intent = new PhotoPickerIntent(this);
                intent.setPhotoCount(9);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
                break;
            case R.id.tv_auction_time:
                triggerPopupWindow();
                break;
        }

    }

    private void triggerPopupWindow() {
        if (popupWindow == null) {
            RelativeLayout rlView = (RelativeLayout) getLayoutInflater().inflate(R.layout.v_popup_window_choose, null);
            RelativeLayout parent = (RelativeLayout) rlView.findViewById(R.id.parent);
            ListView lvChooseTime = (ListView) rlView.findViewById(R.id.lv_choose_time);
            ArrayAdapter<String> popAdapter = new ArrayAdapter<>(mContext, R.layout.item_popup_window_choose, R.id.tv_item_choose_time, itemsAuctionTimeText);
            lvChooseTime.setAdapter(popAdapter);
            popupWindow = new PopupWindow(rlView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//背景不为空但是完全透明
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            lvChooseTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    tvAuctionTime.setText(parent.getAdapter().getItem(position).toString());
                    reqParams.setTime(itemsAuctionTimeNumber[(int) id]);
                }
            });
        }
        popupWindow.showAtLocation(llRoot, Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 0);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                tvAuctionTime.setText(itemsAuctionTimeText[3]);
                tvAuctionTime.setTextColor(getColorById(R.color.c_main));
                tvAuctionTime.setEnabled(true);
                reqParams.setTime(itemsAuctionTimeNumber[3]);
            } else {
                tvAuctionTime.setText(getString(R.string.text_warning));
                tvAuctionTime.setTextColor(getColorById(R.color.orangered));
                tvAuctionTime.setEnabled(false);
                reqParams.setTime(0);
                reqParams.setExpired(true);
            }
        }
    };

    private void judgeAndSubmit() {
        String auctionName = etAuctionName.getText().toString();
        String auctionPrice = etAuctionPrice.getText().toString();
        String auctionDesc = etAuctionDescription.getText().toString();
        if (StringUtils.isEmpty(auctionName)) {
            UiUtil.showToast(mContext, "拍品名不能为空哦(⊙o⊙)");
            UiUtil.popSoftKeyboard(etAuctionName);
        } else if (StringUtils.isEmpty(auctionPrice)) {
            UiUtil.showToast(mContext, "先制定一个起拍价呗。。");
            UiUtil.popSoftKeyboard(etAuctionPrice);
        } else if (selectedPhotos.isEmpty() || selectedPhotos.size() <= 1) {
            UiUtil.showToast(mContext, "添加图片才能发布哦(⊙o⊙)");
        } else {
            String[] picPath = new String[selectedPhotos.size() - 1];
            for (int i = 0; i < selectedPhotos.size() - 1; i++) {
                picPath[i] = selectedPhotos.get(i);
            }
            reqParams.setName(auctionName);
            reqParams.setReservePrice(Double.parseDouble(auctionPrice));
            reqParams.setHammerPrice(null);
            reqParams.setDescription(auctionDesc);
            reqParams.setAccessTimes(0);
            UserInfoBean user = BmobUser.getCurrentUser(mContext, UserInfoBean.class);
            reqParams.setAuctionFirm(user);
            Date date = new Date();
            date.setTime(date.getTime() + reqParams.getTime() * 60 * 1000);
            reqParams.setEndTime(new BmobDate(date));
            reqParams.setExpired(false);
            mPresenter.uploadPic(picPath, reqParams);//如果文件上传成功才继续提交
        }
    }


    @Override
    protected AddAuctionPresenter createdPresenter() {
        return new AddAuctionPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            switch (requestCode) {
                case REQUEST_CODE_SELECT_PHOTO:
                    selectedPhotos.remove("addPic");
                    selectedPhotos.addAll(photos);
                    selectedPhotos.add("addPic");
                    break;
                case REQUEST_CODE_PREVIEW_PHOTO:
                    selectedPhotos.clear();
                    selectedPhotos.addAll(photos);
                    selectedPhotos.add("addPic");
                    break;
            }
            if (adapter == null) {
                gvPhotoSelected.setVisibility(View.VISIBLE);
                rlAddPhoto.setVisibility(View.GONE);
                adapter = new SelectPicResultAdapter(mContext, selectedPhotos);
                gvPhotoSelected.setAdapter(adapter);
            } else {
                adapter.setItems(selectedPhotos);
            }
        }
    }

    @Override
    public void returnAuctionList() {
        setResult(RESULT_CODE_AUCTION_LIST);
        this.finish();
    }

}
