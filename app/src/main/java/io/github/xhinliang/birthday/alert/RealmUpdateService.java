package io.github.xhinliang.birthday.alert;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import io.github.xhinliang.birthday.model.Contact;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xhinliang on 16-3-26.
 * XhinLiang@gmail.com
 */
public class RealmUpdateService extends IntentService {

    private static final String TAG = "RealmUpdateService";

    public RealmUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "RealmUpdateService start");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Contact> contacts = realm.where(Contact.class).findAll();
        // DO NOT use iterator for Realm update.
        for (int i = 0; i < contacts.size(); ++i)
            contacts.get(i).calculateDateRange();
        realm.commitTransaction();
    }
}
