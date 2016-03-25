package io.github.xhinliang.lib.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.github.xhinliang.lib.R;


/**
 * Created by xhinliang on 15-8-30.
 * xhinliang@gmail.com
 * 托管单个Fragment的抽象Activity
 */
public abstract class SingleFragmentActivity extends BaseActivity {


    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (null != getSupportActionBar()) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_main);
        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_main, fragment)
                    .commit();
        }
    }


}