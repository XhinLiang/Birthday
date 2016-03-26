package io.github.xhinliang.birthday;

import android.app.Application;

import io.github.xhinliang.birthday.alert.AlertUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xhinliang on 16-1-29.
 * xhinliang@gmail.com
 */
public class App extends Application {

    private static final long REALM_SCHEMA_VERSION = 0X1d;

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .schemaVersion(REALM_SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        AlertUtils.init(this);
    }

}
