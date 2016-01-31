package io.github.xhinliang.birthday;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import io.github.xhinliang.birthday.model.Group;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xhinliang on 16-1-31.
 * xhinliang@gmail.com
 */
public class JustTest extends Activity {
    private static final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Group group = realm.createObject(Group.class);
        group.setName("FFF");
        realm.commitTransaction();
        RealmResults<Group> groups = realm.where(Group.class).findAll();
        for (Group item : groups) {
            Log.d(TAG, item.getName());
        }
    }
}
