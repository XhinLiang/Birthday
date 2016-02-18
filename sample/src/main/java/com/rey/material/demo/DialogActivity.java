package com.rey.material.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.rey.material.dialog.DatePickerDialog;
import com.rey.material.dialog.Dialog;

import io.github.xhinliang.lunarcalendar.LunarCalendar;

/**
 * Created by xhinliang on 16-2-17.
 * xhinliang@gmail.com
 */
public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker)
                // call sub builder method first
                .dateRange(1, 1, 1900, 1, 1, 2100)
                .positiveAction("OK", new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        LunarCalendar date = ((DatePickerDialog) dialog).getLunarCalendar();
                        Toast.makeText(DialogActivity.this, "Date is " + date.getFullLunarStr(), Toast.LENGTH_SHORT).show();
                    }
                })
                .negativeAction("Cancel", new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        Toast.makeText(DialogActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .build(this)
                .show();
    }
}
