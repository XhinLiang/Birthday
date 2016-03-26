package io.github.xhinliang.birthday.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import io.github.xhinliang.birthday.BuildConfig;
import io.github.xhinliang.birthday.R;
import io.github.xhinliang.lib.util.PreferenceHelper;

/**
 * Created by xhinliang on 16-3-26.
 * XhinLiang@gmail.com
 */
public class AlertUtils {

    public static void init(Context context) {
        String data = PreferenceHelper.getInstance(context)
                .getString(context.getString(R.string.key_alert_time), context.getString(R.string.nothing));
        if (TextUtils.isEmpty(data))
            data = "3";
        int f = Integer.parseInt(data);
        AlertUtils.reschedule(context, f * 60 * 60 * 1000);
    }

    public static void schedule(Context context, long interval) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                interval, makePendingIntent(context));
    }

    public static void stop(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(makePendingIntent(context));
    }

    public static void reschedule(Context context, long interval) {
        stop(context);
        schedule(context, interval);
    }

    private static PendingIntent makePendingIntent(Context context) {
        return PendingIntent.getService(
                context,
                0,
                new Intent(context, AlertService.class).putExtra("debug", BuildConfig.DEBUG),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
