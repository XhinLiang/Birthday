package io.github.xhinliang.birthday;

import android.app.Activity;
import android.os.Bundle;

import io.github.xhinliang.birthday.model.Group;
import io.github.xhinliang.birthday.util.XLog;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by xhinliang on 16-1-31.
 * xhinliang@gmail.com
 */
public class JustTest extends Activity {
    private static final String TAG = "TEST";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        realm.where(Group.class).findAllAsync().asObservable()
                .skip(1)
                .subscribe(new Action1<RealmResults<Group>>() {
                    @Override
                    public void call(RealmResults<Group> groups) {
                        XLog.d(TAG, "Realm launch group result, size " + groups.size());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
