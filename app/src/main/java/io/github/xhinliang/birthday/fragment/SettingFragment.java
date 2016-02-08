package io.github.xhinliang.birthday.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;

import io.github.xhinliang.birthday.R;


/**
 * Created by xhinliang on 16-2-3.
 * xhinliang@gmail.com
 */
public class SettingFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDividerHeight(0);
        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);
    }
}