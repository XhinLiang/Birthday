package io.github.xhinliang.birthday.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.jakewharton.rxbinding.support.design.widget.RxNavigationView;
import com.jakewharton.rxbinding.support.v4.widget.RxDrawerLayout;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.alert.RealmUpdateService;
import io.github.xhinliang.birthday.databinding.ActivityMainBinding;
import io.github.xhinliang.birthday.databinding.IncludeNavHeaderMainBinding;
import io.github.xhinliang.birthday.fragment.ConstantsFragment;
import rx.functions.Action1;

public class MainActivity extends RealmActivity {

    private ActivityMainBinding binding;
    private IncludeNavHeaderMainBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.include_nav_header_main, null, false);
        initView();
        initNavigationSelection();
        startService(new Intent(this, RealmUpdateService.class));
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.addHeaderView(headerBinding.getRoot());
        RxNavigationView.itemSelections(binding.navView)
                .compose(this.<MenuItem>bindToLifecycle())
                .subscribe(new Action1<MenuItem>() {
                    @Override
                    public void call(MenuItem menuItem) {
                        setTabSelection(menuItem.getItemId());
                        closeDrawer();
                    }
                });
    }

    private void initNavigationSelection() {
        binding.navView.setCheckedItem(R.id.drawer_item_contact);
        setTabSelection(R.id.drawer_item_contact);
    }


    private void closeDrawer() {
        RxDrawerLayout.open(binding.drawerLayout, GravityCompat.START).call(false);
    }

    private void setTabSelection(int itemId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.drawer_item_contact:
                hideFragments(transaction);
                if (constantsFragment == null) {
                    constantsFragment = new ConstantsFragment();
                    transaction.add(R.id.fl_content, constantsFragment);
                    break;
                }
                transaction.show(constantsFragment);
                return;
            case R.id.drawer_item_about:
                return;
            case R.id.drawer_item_setting:
                startActivity(new Intent(this, SettingActivity.class));
        }
        transaction.commit();
    }


    private ConstantsFragment constantsFragment;

    private void hideFragments(FragmentTransaction transaction) {
        if (constantsFragment != null) {
            transaction.hide(constantsFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

}
