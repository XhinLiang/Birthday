package io.github.xhinliang.birthday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rey.material.dialog.Dialog;
import com.rey.material.dialog.SimpleDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.github.xhinliang.birthday.alert.AlertUtils;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.util.XLog;
import io.github.xhinliang.lib.util.CalendarUtils;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by xhinliang on 16-1-31.
 * xhinliang@gmail.com
 */
public class JustTest extends AppCompatActivity {
    private static final String TAG = "TEST";
    private Realm realm;
    private RealmResults<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, getDayRange(new GregorianCalendar(2016, Calendar.FEBRUARY, 24)));
    }

    public static String getDayRange(Calendar bornCalendar) {
        return CalendarUtils.computeLunarRange(bornCalendar) + "";
    }

    private void am() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertUtils.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long triggerTime = calendar.getTimeInMillis();
        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, 60000, sender);
    }

    private void dialog() {
        new SimpleDialog.Builder()
                .title("wef")
                .contentView(R.layout.dialog_input)
                .positiveAction(getString(R.string.confirm), new Dialog.Action1() {
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
