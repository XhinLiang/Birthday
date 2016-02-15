package io.github.xhinliang.material.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import io.github.xhinliang.material.R;
import io.github.xhinliang.material.widget.Switch;

public class SwitchPreference extends TwoStatePreference {

    public SwitchPreference(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.init(context, attrs, defStyleAttr, defStyleRes);
        setWidgetLayoutResource(R.layout.mp_switch_preference);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        Switch switchView = (Switch) view.findViewById(R.id.switch_compat);
        switchView.setChecked(isChecked());
        syncSummaryView(view);
    }
}
