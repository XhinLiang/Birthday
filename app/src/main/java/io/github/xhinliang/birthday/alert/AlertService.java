package io.github.xhinliang.birthday.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.activity.MainActivity;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.util.XLog;
import io.github.xhinliang.lib.util.PreferenceHelper;
import io.realm.Realm;

/**
 * Created by xhinliang on 16-3-26.
 * XhinLiang@gmail.com
 */
public class AlertService extends Service {
    private static final String TAG = "AlertService";

    private NotificationManager mNotificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        XLog.d(TAG, "AlertService : onStartCommand");

        String str = PreferenceHelper.getInstance(this).getString(getString(R.string.key_first_alert), getString(R.string.nothing));
        if (TextUtils.isEmpty(str))
            str = "14";
        int firstAlert = Integer.parseInt(str);
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(Contact.class)
                .lessThan("dayRange", firstAlert)
                .count();

        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_done_black_24dp, "Alert " + count, pendingIntent)
                .build();
        mNotificationManager.notify(0, notification);
        return START_STICKY;
    }
}
