package io.github.xhinliang.birthday.fragment;

import android.os.Bundle;

import io.github.xhinliang.mdpreference.PreferenceFragment;

import io.github.xhinliang.birthday.R;


/**
 * Created by xhinliang on 16-2-3.
 * xhinliang@gmail.com
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(getString(R.string.app_name));
        addPreferencesFromResource(R.xml.preference_settings);
    }

}