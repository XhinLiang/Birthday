package io.github.xhinliang.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.MaterialRippleLayout;

import io.github.xhinliang.lib.R;


/**
 * Created by xhinliang on 16-2-3.
 * xhinliang@gmail.com
 */
public class ItemView extends MaterialRippleLayout {
    protected TextView tvTitle;
    protected TextView tvContent;
    protected String title;
    protected String content;

    public ItemView(Context context) {
        super(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemView);
        title = typedArray.getString(R.styleable.ItemView_item_title);
        content = typedArray.getString(R.styleable.ItemView_item_content);
        typedArray.recycle();
        initSubView(context);
    }

    private void initSubView(Context context) {
        View.inflate(context, R.layout.view_item_layout, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        setItemTitle(title);
        setItemContent(content);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);
        return event.getAction() != MotionEvent.ACTION_UP || performClick();
    }

    public void setItemTitle(@NonNull final String title) {
        tvTitle.setText(title);
    }

    public void setItemContent(@NonNull final String content) {
        tvContent.setText(content);
    }

}
