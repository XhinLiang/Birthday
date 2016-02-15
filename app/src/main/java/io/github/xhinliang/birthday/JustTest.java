package io.github.xhinliang.birthday;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Checkable;

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
        setContentView(R.layout.just_test);
        final Checkable switchView = (Checkable) findViewById(R.id.switch_view);
        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switchView.setChecked(!switchView.isChecked());
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
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
