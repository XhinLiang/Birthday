package com.jenzz.materialpreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.*;
import android.util.AttributeSet;

/**
 * Created by xhinliang on 16-2-24.
 */
public class Dp extends Preference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Dp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Dp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Dp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Dp(Context context) {
        super(context);
    }
}
