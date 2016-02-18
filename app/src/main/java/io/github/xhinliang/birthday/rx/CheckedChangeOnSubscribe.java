package io.github.xhinliang.birthday.rx;

import android.os.Looper;
import android.widget.CompoundButton;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.rey.material.widget.CheckBox;

import rx.Observable;
import rx.Subscriber;

final class CheckedChangeOnSubscribe implements Observable.OnSubscribe<Boolean> {
    private final CheckBox checkBox;

    CheckedChangeOnSubscribe(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    public void call(final Subscriber<? super Boolean> subscriber) {
        checkUiThread();

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(isChecked);
                }
            }
        };

        checkBox.setOnCheckedChangeListener(listener);
        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                checkBox.setOnCheckedChangeListener(null);
            }
        });
    }

    private void checkUiThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                    "Must be called from the main thread. Was: " + Thread.currentThread());
        }
    }
}