package com.wyd.auctiontrade.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wyd.auctiontrade.R;


/**
 * Description:自定义Toast
 * Created by wyd on 2016/4/24.
 */

public class CustomToast {

    private Context ctx;
    private String str;
    private int x;
    private int y;

    public CustomToast(Context context, String string) {
        ctx = context;
        str = string;
        x = Gravity.LEFT;
        y = Gravity.TOP + 100;
    }

    public CustomToast(Context context, String string, int y1) {
        ctx = context;
        str = string;
        x = Gravity.LEFT;
        y = y1;
    }

    public CustomToast(Context context, String string, int x1, int y1) {
        ctx = context;
        str = string;
        x = x1;
        y = y1;
    }

    public CustomToast(Context context, int id) {
        ctx = context;
        this.str = context.getResources().getString(id);
        x = Gravity.LEFT + 50;
        y = Gravity.TOP + 100;
    }

    public CustomToast(Context context, int id, int y1) {
        ctx = context;
        this.str = context.getResources().getString(id);
        x = Gravity.LEFT + 50;
        y = y1;
    }

    public CustomToast(Context context, int id, int x1, int y1) {
        ctx = context;
        this.str = context.getResources().getString(id);
        x = x1;
        y = y1;
    }

    public void show() {
        Toast toast = new Toast(ctx);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.toast, null);
        contentView.setBackgroundResource(R.drawable.bg_toast);
        TextView text1 = (TextView) contentView.findViewById(R.id.tv_toast);
        text1.setText(str);
        toast.setView(contentView);
//		toast.setGravity(Gravity.BOTTOM, x, y);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void show(int time) {
        Toast toast = new Toast(ctx);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.toast, null);
        TextView text1 = (TextView) contentView.findViewById(R.id.tv_toast);
        text1.setText(str);
        toast.setView(contentView);
        toast.setGravity(Gravity.BOTTOM, x, y);
        toast.setDuration(time);
        toast.show();
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
