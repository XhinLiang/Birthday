package io.github.xhinliang.birthday.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.dialog.Dialog;
import com.rey.material.dialog.SimpleDialog;

import org.parceler.Parcels;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.activity.ContactDetailsActivity;
import io.github.xhinliang.birthday.adapter.ContactAdapter;
import io.github.xhinliang.birthday.databinding.FragmentContactsBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.model.Group;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by xhinliang on 16-3-25.
 * XhinLiang@gmail.com
 */
public class ConstantsFragment extends RxFragment<FragmentContactsBinding> {
    private RealmResults<Contact> contacts;
    private RealmResults<Group> groups;
    private ContactAdapter adapter;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Nullable
    @Override
    public FragmentContactsBinding onCreateBinding
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initEvent();
    }

    private void initRecyclerView() {
        contacts = realm.where(Contact.class).findAllAsync();
        groups = realm.where(Group.class).findAllAsync();
        adapter = new ContactAdapter(getActivity(), contacts, new ContactAdapter.Listener() {
            @Override
            public void onContactItemClick(Contact contact) {
                Intent intent = new Intent(getActivity(), ContactDetailsActivity.class);
                intent.putExtra(ContactDetailsActivity.EXTRA_CONTACT, Parcels.wrap(contact));
                startActivity(intent);
            }
        });
        binding.rvContacts.setAdapter(adapter);
    }

    private void initEvent() {
        setRxClick(binding.fabAdd)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), ContactDetailsActivity.class));
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_main, menu);
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

    private void selectGroup() {
        if (!groups.isLoaded()) {
//            ((MainActivity) getActivity()).showSimpleDialog(getString(R.string.group_loading));
            return;
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
                .build(getActivity())
                .show();
    }


}
