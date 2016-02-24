package io.github.xhinliang.birthday.fragment;

import android.os.Bundle;

import com.jenzz.materialpreference.PreferenceFragment;

import io.github.xhinliang.birthday.R;


/**
 * Created by xhinliang on 16-2-3.
 * xhinliang@gmail.com
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
    }


}