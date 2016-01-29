package io.github.xhinliang.birthday.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.jakewharton.rxbinding.support.design.widget.RxNavigationView;
import com.jakewharton.rxbinding.support.v4.widget.RxDrawerLayout;

import java.util.LinkedList;
import java.util.List;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.databinding.ActivityMainBinding;
import io.github.xhinliang.birthday.databinding.IncludeNavHeaderMainBinding;
import io.github.xhinliang.lib.activity.BaseActivity;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private IncludeNavHeaderMainBinding headerBinding;
    private List<CharSequence> contactGroups = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        headerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.include_nav_header_main, null, false);
        binding.navView.addHeaderView(headerBinding.getRoot());

        RxNavigationView.itemSelections(binding.navView)
                .compose(this.<MenuItem>bindToLifecycle())
                .subscribe(new Action1<MenuItem>() {
                    @Override
                    public void call(MenuItem menuItem) {
                        setTabSelection(menuItem);
                        RxDrawerLayout.open(binding.drawerLayout, GravityCompat.START)
                                .call(false);
                    }
                });
        initNavigationSelection();
        initEvent();
    }

    private void initEvent() {
        setRxClick(binding.fabAdd)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(MainActivity.this, NewContactActivity.class));
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initContactGroups();
    }

    private void initContactGroups() {
        contactGroups.clear();
        //在数据库中取出联系人分组列表数据
        for (CharSequence title : contactGroups)
            binding.navView.getMenu().add(title);
    }

    private void initNavigationSelection() {
        binding.navView.setCheckedItem(R.id.all_friend);
    }

    private void setTabSelection(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.about:
                return;
            case R.id.me:
                return;
            case R.id.setting:
                return;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }
}
