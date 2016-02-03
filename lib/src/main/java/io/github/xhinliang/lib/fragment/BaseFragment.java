package io.github.xhinliang.lib.fragment;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.concurrent.TimeUnit;

import rx.Observable;


/**
 * Created by xhinliang on 15-11-24.
 * xhinliang@gmail.com
 */
public class BaseFragment extends RxFragment {
    protected Observable<Void> setRxView(View view) {
        return RxView.clicks(view)
                .throttleFirst(500, TimeUnit.MILLISECONDS);
    }
}
