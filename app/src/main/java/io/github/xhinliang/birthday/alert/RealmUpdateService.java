package io.github.xhinliang.birthday.alert;

import android.app.IntentService;
import android.content.Intent;

import io.github.xhinliang.birthday.model.Contact;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xhinliang on 16-3-26.
 * XhinLiang@gmail.com
 */
public class RealmUpdateService extends IntentService {

    public RealmUpdateService() {
        super("RealmUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Contact> contacts = realm.where(Contact.class).findAll();
        for(Contact contact : contacts){
            contact.calculateDateRange();
        }
    }
}
