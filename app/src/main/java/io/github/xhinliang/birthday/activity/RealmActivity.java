package io.github.xhinliang.birthday.activity;

import android.os.Bundle;

import io.github.xhinliang.lib.activity.BaseActivity;
import io.realm.Realm;


/**
 * Created by xhinliang on 16-2-1.
 * xhinliang@gmail.com
 */
public class RealmActivity extends BaseActivity {
    protected Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
