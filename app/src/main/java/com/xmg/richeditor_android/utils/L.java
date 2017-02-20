package com.xmg.richeditor_android.utils;

import android.util.Log;

/**
 * 作者： Administrator on 2015/9/8.
 * 邮箱：maxz@tingwin.com
 */
public class L {


    private L() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static final boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "xmg";

    /**
     * 是否处于调试模式
     */
    public static boolean isDebuggable() {
        return isDebug;
    }


    //执行打印
    private static void performPrint(int level, String tag, String msg) {
        //非Debug版本，则不打印日志
        if (!isDebuggable()) {
            return;
        }
        String threadName = Thread.currentThread().getName();
        String lineIndicator = getLineIndicator();
        Log.println(level, tag, threadName + " " + lineIndicator + " " + msg);
    }

    //获取行所在的方法指示
    //获取行所在的方法指示
    private static String getLineIndicator() {
        //3代表方法的调用深度：0-getLineIndicator，1-performPrint，2-print，3-调用该工具类的方法位置
        StackTraceElement element = ((new Exception()).getStackTrace())[3];
        StringBuffer sb = new StringBuffer("(")
                .append(element.getFileName()).append(":")
                .append(element.getLineNumber()).append(").")
                .append(element.getMethodName()).append(":");
        return sb.toString();
    }


    public static void v(String message) {
        if (!isDebuggable()) {
            return;
        }

        performPrint(Log.VERBOSE, TAG, message);
    }

    public static void d(String message) {
        if (!isDebuggable()) {
            return;
        }


        performPrint(Log.DEBUG, TAG, message);
    }

    public static void d(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }


        performPrint(Log.DEBUG, TAG, message);
    }

    public static void i(String message) {
        if (!isDebuggable()) {
            return;
        }


        performPrint(Log.INFO, TAG, message);
    }

    public static void i(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        performPrint(Log.INFO, TAG, message);
    }

    public static void w(String message) {
        if (!isDebuggable()) {
            return;
        }
        performPrint(Log.WARN, TAG, message);
    }
    public static void w(String tag,String message) {
        if (!isDebuggable()) {
            return;
        }
        performPrint(Log.WARN, TAG, message);
    }

    public static void e(String message) {
        if (!isDebuggable()) {
            return;
        }

        performPrint(Log.ERROR, TAG, message);
    }
    public static void e(String tag,String message) {
        if (!isDebuggable()) {
            return;
        }

        performPrint(Log.ERROR, TAG, message);
    }

    public static int getGBS(int x, int y) {
        for (int i = 1; i <= x * y; i++) {
            if (i % x == 0 && i % y == 0)
                return i;
        }

        return x * y;
    }
}
