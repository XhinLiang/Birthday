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

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.adapter.ContactAdapter;
import io.github.xhinliang.birthday.databinding.ActivityMainBinding;
import io.github.xhinliang.birthday.databinding.IncludeNavHeaderMainBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.model.Group;
import io.github.xhinliang.birthday.util.XLog;
import io.github.xhinliang.lib.activity.RealmActivity;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.functions.Action1;

public class MainActivity extends RealmActivity implements ContactAdapter.Listener {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private IncludeNavHeaderMainBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.include_nav_header_main, null, false);
        initView();
        initNavigationSelection();
        initEvent();
        initContactGroups();
        initRecyclerView();
    }

    private void initRecyclerView() {
        final RealmResults<Contact> contacts = realm.where(Contact.class).findAllAsync();
        binding.rvContacts.setAdapter(new ContactAdapter(this, contacts, this));

        contacts.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                for (Contact item : contacts)
                    if (item.getPicture() != null)
                        XLog.d(TAG, item.getPicture());
            }
        });
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
                        RxDrawerLayout.open(binding.drawerLayout, GravityCompat.START).call(false);
                    }
                });
    }

    private void initEvent() {
        setRxClick(binding.fabAdd)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(MainActivity.this, AddContactActivity.class));
                    }
                });
    }

    private void initContactGroups() {
        realm.where(Group.class)
                .findAllAsync()
                .asObservable()
                .compose(this.<RealmResults<Group>>bindToLifecycle())
                .subscribe(new Action1<RealmResults<Group>>() {
                    @Override
                    public void call(RealmResults<Group> groups) {
                        for (int i = 1; i < 10; ++i)
                            binding.navView.getMenu().getItem(i).setVisible(false);
                        for (int i = 0; i < groups.size(); ++i) {
                            MenuItem item = binding.navView.getMenu().getItem(i + 1);
                            item.setTitle(groups.get(i).getName());
                            item.setVisible(true);
                        }
                    }
                });
    }

    private void initNavigationSelection() {
        binding.navView.setCheckedItem(R.id.menu_item_all_friend);
    }

    private void setTabSelection(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.about:
                return;
            case R.id.me:
                return;
            case R.id.setting:
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

    @Override
    public void onUserItemClick(ContactAdapter.ViewHolder holder) {

    }
}
