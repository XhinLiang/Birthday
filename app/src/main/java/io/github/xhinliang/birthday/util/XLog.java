package io.github.xhinliang.birthday.util;

import android.util.Log;

import io.github.xhinliang.birthday.BuildConfig;

/**
 * Created by xhinliang on 16-2-1.
 * xhinliang@gmail.com
 */
public class XLog {
    public static void d(String TAG, String text) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, text);
    }

    public static void i(String TAG, String text) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, text);
    }

    public static void v(String TAG, String text) {
        if (BuildConfig.DEBUG)
            Log.v(TAG, text);
    }
}
