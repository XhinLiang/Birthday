package io.github.xhinliang.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xhinliang on 15-12-1.
 * xhinliang@gmail.com
 */
public class StatusBarHolderView extends View {

    private final int height;

    public StatusBarHolderView(Context context) {
        this(context, null);
    }

    public StatusBarHolderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        height = determineStatusBarHeight();
    }

    private int determineStatusBarHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }

        if (isInEditMode()) {
            return (int) (25 * getContext().getResources().getDisplayMetrics().density);
        }

        boolean hasStatusBar = false;

        TypedArray a = getContext().obtainStyledAttributes(new int[]{
                android.R.attr.windowTranslucentStatus
        });

        try {
            hasStatusBar = a.getBoolean(0, false);
        } finally {
            a.recycle();
        }

        int resId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (hasStatusBar && resId > 0) {
            return getContext().getResources().getDimensionPixelSize(resId);
        } else {
            return 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

}
