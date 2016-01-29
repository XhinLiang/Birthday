package io.github.xhinliang.birthday;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xhinliang on 16-1-29.
 * xhinliang@gmail.com
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
