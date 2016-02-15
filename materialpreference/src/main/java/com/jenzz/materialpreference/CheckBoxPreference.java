package com.jenzz.materialpreference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import static com.jenzz.materialpreference.ThemeUtils.isAtLeastL;

public class CheckBoxPreference extends TwoStatePreference {

    public CheckBoxPreference(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public CheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public CheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.init(context, attrs, defStyleAttr, defStyleRes);
        setWidgetLayoutResource(R.layout.mp_checkbox_preference);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onBindView(View view) {
        super.onBindView(view);
        CheckBox checkboxView = (CheckBox) view.findViewById(R.id.checkbox);
        checkboxView.setChecked(isChecked());
        if (isAtLeastL()) {
            // remove circular background when pressed
            checkboxView.setBackgroundDrawable(null);
        }
        syncSummaryView();
    }
}
