package io.github.xhinliang.birthday;

import android.app.Activity;
import android.os.Bundle;

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
        realm = Realm.getDefaultInstance();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
