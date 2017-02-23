package com.wyd.auctiontrade.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wyd.auctiontrade.R;
import com.wyd.auctiontrade.view.CustomToast;

import java.util.regex.Pattern;

/**
 * Description:封装与UI相关的公共方法
 * Created by wyd on 2016/4/24.
 */
public class UiUtil {
    private final static Pattern phone = Pattern.compile("^1[34578]\\d{9}$");

    public static Dialog progressDialog;

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        return phone.matcher(phoneNum).matches();
    }


    /**
     * 自定义Toast
     */
    public static void showToast(Context context, String message) {
        CustomToast toast = new CustomToast(context, message);
        toast.show();
    }


    /**
     * 自定义Toast
     */
    public static void showToast(Context context, int resId) {
        CustomToast toast = new CustomToast(context, resId);
        toast.show();
    }


    /**
     * 显示loading框
     */
    public static void showRoundProcessDialog(Context context, boolean isCancel) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                return;
            }
        }
        progressDialog = new AlertDialog.Builder(context, R.style.dialog).create();
        progressDialog.setCancelable(isCancel);
        progressDialog.show();
        // 注意此处要放在show之后 否则会报异常
        progressDialog.setContentView(R.layout.loading_process_dialog_icon);
    }


    /**
     * 关闭loading框
     */
    public static void dismissProcessDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    /**
     * EditText控件获取焦点并自动弹出软键盘
     */
    public static void popSoftKeyboard(EditText et) {
        et.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, 0);
    }

}
