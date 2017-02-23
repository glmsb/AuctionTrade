package com.wyd.auctiontrade.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 活动管理器类
 */
public class ActivityCollector {

    public static List<Activity> activitys = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activity.finish();
        activitys.remove(activity);
    }

    /**
     * 不管你想在什么地方退出程序，只需要调用 ActivityCollector.finishAll()方法就可以了
     */
    public static void finishAll() {
        for (Activity activity : activitys) {
            if (!activity.isFinishing()) {//判断当前活动是否已结束
                activity.finish();
            }
        }
    }
}
