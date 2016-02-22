package io.github.xhinliang.birthday;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Checkable;

import com.rey.material.dialog.Dialog;
import com.rey.material.dialog.SimpleDialog;

import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.util.XLog;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by xhinliang on 16-1-31.
 * xhinliang@gmail.com
 */
public class JustTest extends Activity {
    private static final String TAG = "TEST";
    private Realm realm;
    private RealmResults<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SimpleDialog.Builder()
                .title(getString(R.string.select_group))
                .positiveAction(getString(R.string.select), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                    }
                })
                .neutralAction(getString(R.string.create), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                    }
                })
                .negativeAction(getString(R.string.cancel), null)
                .build(this)
                .show();
    }

    private void realm() {
        contacts = realm.where(Contact.class)
                .findAllAsync();

        contacts.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                XLog.d(TAG, "====================ONCHANGE===========================");
                for (Contact item : contacts)
                    XLog.d(TAG, item.getName() + " " + item.getGroup());
                XLog.d(TAG, "====================ONCHANGE===========================");
            }
        });

        contacts.clear();
    }

}
