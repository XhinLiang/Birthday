package io.github.xhinliang.lib.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.github.xhinliang.lib.R;
import rx.functions.Func1;

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
        new AlertDialog.Builder(this)
                .setMessage(content)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void showSimpleDialog(int titleRes, int contentRes) {
        new AlertDialog.Builder(this)
                .setMessage(contentRes)
                .setTitle(titleRes)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void showSimpleDialog(int titleRes, CharSequence content) {
        new AlertDialog.Builder(this)
                .setMessage(content)
                .setTitle(titleRes)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void showSimpleDialog(int contentRes) {
        new AlertDialog.Builder(this)
                .setMessage(contentRes)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public class SpinnerFilter implements Func1<Void, Boolean> {
        AppCompatSpinner spinner;
        int messageRes;

        public SpinnerFilter(AppCompatSpinner spinner, int messageRes) {
            this.spinner = spinner;
            this.messageRes = messageRes;
        }

        @Override
        public Boolean call(Void aVoid) {
            if (spinner.getSelectedItemPosition() == 0) {
                showSimpleDialog(messageRes);
                return false;
            }
            return true;
        }
    }

    public class EditTextFilter implements Func1<Void, Boolean> {
        EditText editText;
        int messageRes;

        public EditTextFilter(EditText editText, int messageRes) {
            this.editText = editText;
            this.messageRes = messageRes;
        }

        @Override
        public Boolean call(Void viewClickEvent) {
            if (editText.getText().toString().isEmpty()) {
                showSimpleDialog(messageRes);
                return false;
            }
            return true;
        }
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
