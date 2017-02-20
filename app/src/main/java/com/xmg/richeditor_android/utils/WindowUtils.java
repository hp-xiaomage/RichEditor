package com.xmg.richeditor_android.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2015/12/1.
 */
public class WindowUtils {
    public static int getWidth(Activity context){
        WindowManager wm = context.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return width;
    }

    public static int getHeight(Activity context){
        WindowManager wm = context.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return height;
    }


    public static int getDisplayMetricsWidth(Activity context){
        DisplayMetrics  dm = new DisplayMetrics();
        //取得窗口属性
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        int screenWidth = dm.widthPixels;

        //窗口高度
        int screenHeight = dm.heightPixels;
        return screenWidth;
    }

    public static int getDisplayMetricsHeight(Activity context){
        DisplayMetrics  dm = new DisplayMetrics();
        //取得窗口属性
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        int screenWidth = dm.widthPixels;

        //窗口高度
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }
}
