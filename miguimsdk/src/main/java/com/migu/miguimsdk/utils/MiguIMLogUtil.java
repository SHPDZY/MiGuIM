package com.migu.miguimsdk.utils;

import android.util.Log;

import com.migu.miguimsdk.BuildConfig;


public class MiguIMLogUtil {
    public static final String TAG_WIDGET = "MiGu_IM";
    private static final boolean DEBUG = BuildConfig.DEBUG ? true : false;
    private static final int LOG_ASSERT = 7;
    public enum MGSVLogLevel{
        LOG_VERBOSE(2), LOG_DEBUG(3),LOG_INFO(4),LOG_WARN(5),LOG_ERROR(6),LOG_SILENT(8);

        private final int mLevel;
        MGSVLogLevel(int level) {
            mLevel = level;
        }

        public int getLogLevel() {
            return mLevel;
        }
    }

    /**
     *打印info信息
     */
    public static int i( String msg) {
        if(DEBUG) {
            return Log.i(TAG_WIDGET, msg);
        }
        return LOG_ASSERT;
    }
    /**
     *打印info信息
     */
    public static int i( String msg, Throwable e) {
        if(DEBUG) {
            return Log.i(TAG_WIDGET, msg, e);
        }
        return LOG_ASSERT;
    }
    /**
     *打印verbose信息
     */
    public static int v( String msg) {
        if(DEBUG) {
            return Log.v(TAG_WIDGET, msg);
        }
        return LOG_ASSERT;
    }

    /**
     *打印verbose信息
     */
    public static int v( String msg, Throwable e) {
        if(DEBUG) {
            return Log.v(TAG_WIDGET, msg, e);
        }
        return LOG_ASSERT;
    }

    /**
     *打印debug信息
     */
    public static int d( String msg) {
        if(DEBUG) {
            return Log.d(TAG_WIDGET, msg);
        }
        return LOG_ASSERT;
    }

    /**
     *打印debug信息
     */
    public static int d( String msg, Throwable e) {
        if(DEBUG) {
            return Log.d(TAG_WIDGET, msg, e);
        }
        return LOG_ASSERT;
    }

    /**
     *打印warn信息
     */
    public static int w( String msg) {
        if(DEBUG) {
            return Log.w(TAG_WIDGET, msg);
        }
        return LOG_ASSERT;

    }

    /**
     * 将抛出的警告信息打印出来
     * 
     * @param msg log信息
     * @param e  抛出的警告信息
     */
    public static int w( String msg, Throwable e) {
        if(DEBUG) {
            return Log.w(TAG_WIDGET, msg, e);
        }
        return LOG_ASSERT;
    }

    /**
     * 打印error级别的log
     */
    public static int e( String msg) {
        if(DEBUG) {
            return Log.e(TAG_WIDGET, msg);
        }
        return LOG_ASSERT;
    }
    /**
     * 将抛出的错误异常打印出来
     * 
     * @param msg log信息
     * @param e  抛出的错误异常信息
     */
    public static int e( String msg, Throwable e) {
        if(DEBUG) {
            return Log.e(TAG_WIDGET, msg, e);
        }
        return LOG_ASSERT;
    }

}
