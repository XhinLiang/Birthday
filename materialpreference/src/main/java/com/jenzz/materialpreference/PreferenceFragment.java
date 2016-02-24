package com.jenzz.materialpreference;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class PreferenceFragment extends android.preference.PreferenceFragment {

    private static final int PADDING_LEFT_RIGHT = 5;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setPadding(PADDING_LEFT_RIGHT, 0, PADDING_LEFT_RIGHT, 0);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDividerHeight(0);
        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);
    }
}
