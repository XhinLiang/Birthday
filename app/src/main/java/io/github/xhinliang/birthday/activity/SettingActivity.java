package io.github.xhinliang.birthday.activity;


import android.app.Fragment;
import android.os.Bundle;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.alert.AlertUtils;
import io.github.xhinliang.birthday.fragment.SettingsFragment;
import io.github.xhinliang.lib.activity.SingleFragmentActivity;

public class SettingActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.settings);
    }

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AlertUtils.init(this);
    }
}
