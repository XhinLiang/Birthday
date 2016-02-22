package io.github.xhinliang.lib.activity;

import android.view.MenuItem;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.rey.material.dialog.Dialog;
import com.rey.material.dialog.SimpleDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.github.xhinliang.lib.R;

/**
 * Created by xhinliang on 15-11-23.
 * xhinliang@gmail.com
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected void setHasBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    public rx.Observable<Void> setRxClick(View view) {
        return RxView.clicks(view)
                .throttleFirst(500, TimeUnit.MILLISECONDS);
    }


    protected void showSimpleDialog(CharSequence content) {
        new SimpleDialog.Builder()
                .message(content)
                .positiveAction(getString(R.string.confirm), null)
                .build(this)
                .show();
    }

    protected void showSimpleDialog(int titleRes, int contentRes) {
        new SimpleDialog.Builder()
                .message(getString(contentRes))
                .title(getString(titleRes))
                .positiveAction(getString(R.string.confirm), null)
                .build(this)
                .show();
    }

    protected void showSimpleDialog(int titleRes, int contentRes, Dialog.Action1 callback) {
        new SimpleDialog.Builder()
                .message(getString(contentRes))
                .title(getString(titleRes))
                .positiveAction(getString(R.string.confirm), callback)
                .build(this)
                .show();
    }

    protected void showSimpleDialog(int contentRes) {
        new SimpleDialog.Builder()
                .message(getString(contentRes))
                .positiveAction(getString(R.string.confirm), null)
                .build(this)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
