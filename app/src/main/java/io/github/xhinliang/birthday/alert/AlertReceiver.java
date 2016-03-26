package io.github.xhinliang.birthday.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by xhinliang on 16-3-26.
 * XhinLiang@gmail.com
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlertUtils.init(context);
    }
}
