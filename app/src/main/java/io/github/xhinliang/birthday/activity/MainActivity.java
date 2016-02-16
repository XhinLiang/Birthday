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

import org.parceler.Parcels;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.adapter.ContactAdapter;
import io.github.xhinliang.birthday.databinding.ActivityMainBinding;
import io.github.xhinliang.birthday.databinding.IncludeNavHeaderMainBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.lib.activity.RealmActivity;
import io.realm.RealmResults;
import rx.functions.Action1;

public class MainActivity extends RealmActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private IncludeNavHeaderMainBinding headerBinding;
    private RealmResults<Contact> contacts;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.include_nav_header_main, null, false);
        initView();
        initNavigationSelection();
        initEvent();
        initRecyclerView();
    }

    private void initRecyclerView() {
        contacts = realm.where(Contact.class).findAllAsync();
        adapter = new ContactAdapter(this, contacts, new ContactAdapter.Listener() {
            @Override
            public void onContactItemClick(Contact contact) {
                Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                intent.putExtra(ContactDetailsActivity.EXTRA_CONTACT, Parcels.wrap(contact));
                startActivity(intent);
            }
        });
        binding.rvContacts.setAdapter(adapter);
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        binding.navView.addHeaderView(headerBinding.getRoot());

        RxNavigationView.itemSelections(binding.navView)
                .compose(this.<MenuItem>bindToLifecycle())
                .subscribe(new Action1<MenuItem>() {
                    @Override
                    public void call(MenuItem menuItem) {
                        setTabSelection(menuItem);
                        closeDrawer();
                    }
                });
    }

    private void closeDrawer() {
        RxDrawerLayout.open(binding.drawerLayout, GravityCompat.START).call(false);
    }

    private void initEvent() {
        setRxClick(binding.fabAdd)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(MainActivity.this, ContactDetailsActivity.class));
                    }
                });
    }


    private void initNavigationSelection() {
        binding.navView.setCheckedItem(R.id.menu_item_all_friend);
    }

    private void setTabSelection(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_all_friend:
                resetAdapterData();
                return;
            case R.id.menu_item_about:
                return;
            case R.id.menu_item_setting:
                startActivity(new Intent(this, SettingActivity.class));
                return;
        }
        String groupName = menuItem.getTitle().toString();
        resetAdapterData(groupName);
    }

    private void resetAdapterData(String groupName) {
        contacts = realm.where(Contact.class)
                .equalTo(Contact.FIELD_GROUP, groupName)
                .findAllAsync();
        adapter.resetDataSet(contacts);
    }

    private void resetAdapterData() {
        contacts = realm.where(Contact.class).findAllAsync();
        adapter.resetDataSet(contacts);
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
