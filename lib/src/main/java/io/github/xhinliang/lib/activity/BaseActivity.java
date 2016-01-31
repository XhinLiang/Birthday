package io.github.xhinliang.lib.activity;

import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.view.RxView;
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
        new MaterialDialog.Builder(this)
                .content(content)
                .positiveText(R.string.confirm)
                .build()
                .show();
    }

    protected void showSimpleDialog(int titleRes, int contentRes) {
        new MaterialDialog.Builder(this)
                .title(titleRes)
                .content(contentRes)
                .positiveText(R.string.confirm)
                .build()
                .show();
    }

    protected void showSimpleDialog(int titleRes, CharSequence content) {
        new MaterialDialog.Builder(this)
                .title(titleRes)
                .content(content)
                .positiveText(R.string.confirm)
                .build()
                .show();
    }

    protected void showSimpleDialog(int contentRes) {
        new MaterialDialog.Builder(this)
                .content(contentRes)
                .positiveText(R.string.confirm)
                .build()
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
