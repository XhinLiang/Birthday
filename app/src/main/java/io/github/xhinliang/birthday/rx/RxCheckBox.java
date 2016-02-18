package io.github.xhinliang.birthday.rx;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.rey.material.widget.CheckBox;

import rx.Observable;

/**
 * Created by xhinliang on 16-2-3.
 * xhinliang@gmail.com
 */
public class RxCheckBox {
    @CheckResult
    @NonNull
    public static Observable<Boolean> checkedChange(@NonNull CheckBox checkBox) {
        return Observable.create(new CheckedChangeOnSubscribe(checkBox));
    }
}
