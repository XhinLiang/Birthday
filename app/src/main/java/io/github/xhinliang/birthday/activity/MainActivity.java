package io.github.xhinliang.birthday.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.jakewharton.rxbinding.support.design.widget.RxNavigationView;
import com.jakewharton.rxbinding.support.v4.widget.RxDrawerLayout;
import com.rey.material.dialog.Dialog;
import com.rey.material.dialog.SimpleDialog;

import org.parceler.Parcels;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.adapter.ContactAdapter;
import io.github.xhinliang.birthday.databinding.ActivityMainBinding;
import io.github.xhinliang.birthday.databinding.IncludeNavHeaderMainBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.model.Group;
import io.github.xhinliang.lib.activity.RealmActivity;
import io.realm.RealmResults;
import rx.functions.Action1;

public class MainActivity extends RealmActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private IncludeNavHeaderMainBinding headerBinding;
    private RealmResults<Contact> contacts;
    private RealmResults<Group> groups;
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
        groups = realm.where(Group.class).findAllAsync();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_select_group:
                selectGroup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectGroup() {
        if (!groups.isLoaded()) {
            showSimpleDialog(getString(R.string.group_loading));
        }
        int size = groups.size();
        final String[] groupNames = new String[size + 1];
        groupNames[0] = getString(R.string.all);
        for (int i = 0; i < size; ++i) {
            groupNames[i + 1] = groups.get(i).getName();
        }

        new SimpleDialog.Builder()
                .items(groupNames, 0)
                .positiveAction(getString(R.string.confirm), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        int index = ((SimpleDialog) dialog).getSelectedIndex();
                        if (index != 0)
                            resetAdapterData(((SimpleDialog) dialog).getSelectedValue().toString());
                        else
                            resetAdapterData();
                    }
                })
                .negativeAction(getString(R.string.cancel), null)
                .title(getString(R.string.select_group))
                .build(this)
                .show();
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
        binding.navView.setCheckedItem(R.id.drawer_item_contact);
    }

    private void setTabSelection(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_item_contact:
                resetAdapterData();
                return;
            case R.id.drawer_item_about:
                return;
            case R.id.drawer_item_setting:
                startActivity(new Intent(this, SettingActivity.class));
        }
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
