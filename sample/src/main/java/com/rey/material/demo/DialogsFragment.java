package com.rey.material.demo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rey.material.dialog.BottomSheetDialog;
import com.rey.material.dialog.DatePickerDialog;
import com.rey.material.dialog.Dialog;
import com.rey.material.dialog.DialogFragment;
import com.rey.material.dialog.SimpleDialog;
import com.rey.material.dialog.TimePickerDialog;
import com.rey.material.drawable.ThemeDrawable;
import com.rey.material.util.ThemeManager;
import com.rey.material.util.ViewUtil;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import java.text.SimpleDateFormat;

public class DialogsFragment extends Fragment implements View.OnClickListener {

    public static DialogsFragment newInstance() {
        return new DialogsFragment();
    }

    private MainActivity mActivity;

    private BottomSheetDialog mBottomSheetDialog;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        Button bt_title_only = (Button) v.findViewById(R.id.dialog_bt_title_only);
        Button bt_msg_only = (Button) v.findViewById(R.id.dialog_bt_msg_only);
        Button bt_title_msg = (Button) v.findViewById(R.id.dialog_bt_title_msg);
        Button bt_custom = (Button) v.findViewById(R.id.dialog_bt_custom);
        Button bt_choice = (Button) v.findViewById(R.id.dialog_bt_choice);
        Button bt_multi_choice = (Button) v.findViewById(R.id.dialog_bt_multi_choice);
        Button bt_time = (Button) v.findViewById(R.id.dialog_bt_time);
        Button bt_date = (Button) v.findViewById(R.id.dialog_bt_date);
        Button bt_bottomsheet = (Button) v.findViewById(R.id.dialog_bt_bottomsheet);

        bt_title_only.setOnClickListener(this);
        bt_msg_only.setOnClickListener(this);
        bt_title_msg.setOnClickListener(this);
        bt_custom.setOnClickListener(this);
        bt_choice.setOnClickListener(this);
        bt_multi_choice.setOnClickListener(this);
        bt_time.setOnClickListener(this);
        bt_date.setOnClickListener(this);
        bt_bottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
        mActivity = (MainActivity) getActivity();
        return v;
    }

    private void showBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(mActivity, R.style.Material_App_BottomSheetDialog);
        View v = LayoutInflater.from(mActivity).inflate(R.layout.view_bottomsheet, null);
        ViewUtil.setBackground(v, new ThemeDrawable(R.array.bg_window));
        Button bt_match = (Button) v.findViewById(R.id.sheet_bt_match);
        bt_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.heightParam(ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });
        Button bt_wrap = (Button) v.findViewById(R.id.sheet_bt_wrap);
        bt_wrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        mBottomSheetDialog
                .contentView(v)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismissImmediately();
            mBottomSheetDialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        Dialog.Builder builder;
        DialogFragment fragment;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;

        switch (v.getId()) {
            case R.id.dialog_bt_title_only:
                new SimpleDialog.Builder()
                        .title("Discard draft?")
                        .positiveAction("DISCARD", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Discarded", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .negativeAction("CANCEL", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Canceled", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build(mActivity)
                        .show();
                break;
            case R.id.dialog_bt_msg_only:
                new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog)
                        .message("Delete this conversation?")
                        .positiveAction("DELETE", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .negativeAction("CANCEL", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Cancelled", Toast.LENGTH_SHORT).show();

                            }
                        }).build(mActivity).show();
                break;
            case R.id.dialog_bt_title_msg:
                new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog)
                        .message("Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
                        .title("Use Google's location service?")
                        .positiveAction("AGREE", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Agreed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .negativeAction("DISAGREE", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Disagreed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build(mActivity)
                        .show();
                break;
            case R.id.dialog_bt_custom:
                builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog)
                        .title("Google Wi-Fi")
                        .contentView(R.layout.layout_dialog_custom)
                        .positiveAction("CONNECT", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                EditText et_pass = (EditText) dialog.findViewById(R.id.custom_et_password);
                                Toast.makeText(mActivity, "Connected. pass=" + et_pass.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .negativeAction("CANCEL", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                fragment = DialogFragment.newInstance(builder);
                fragment.show(getFragmentManager(), null);
                break;

            case R.id.dialog_bt_choice:
                builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog)
                        .items(new String[]{"None", "Callisto", "Dione", "Ganymede", "Hangouts Call", "Luna", "Oberon", "Phobos"}, 0)
                        .positiveAction("OK", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "You have selected " + ((SimpleDialog) dialog).getSelectedValue() + " as phone ringtone.", Toast.LENGTH_SHORT).show();
                            }
                        }).negativeAction("Cancel", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }).title("Phone Ringtone");
                fragment = DialogFragment.newInstance(builder);
                fragment.show(getFragmentManager(), null);
                break;

            case R.id.dialog_bt_multi_choice:
                new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog)
                        .multiChoiceItems(new String[]{"Soup", "Pizza", "Hot dog", "Hamburger", "Coffee", "Juice", "Milk", "Water"}, 2, 5)
                        .positiveAction("OK", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                CharSequence[] values = ((SimpleDialog) dialog).getSelectedValues();
                                if (values == null)
                                    Toast.makeText(mActivity, "You have selected nothing.", Toast.LENGTH_SHORT).show();
                                else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("You have selected ");
                                    for (int i = 0; i < values.length; i++)
                                        sb.append(values[i]).append(i == values.length - 1 ? "." : ", ");
                                    Toast.makeText(mActivity, sb.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .negativeAction("Cancel", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .title("Food Order")
                        .build(getActivity())
                        .show();
                break;


            case R.id.dialog_bt_time:
                new TimePickerDialog.Builder(isLightTheme ? R.style.Material_App_Dialog_TimePicker_Light : R.style.Material_App_Dialog_TimePicker, 24, 0)
                        .positiveAction("OK", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Time is " + ((TimePickerDialog) dialog).getFormattedTime(SimpleDateFormat.getTimeInstance()), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .negativeAction("CANCEL", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Cancelled", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build(getActivity())
                        .show();
                break;
            // You can use DialogFragment neither.
            // always call sub builder method first
            case R.id.dialog_bt_date:
                builder = new DatePickerDialog.Builder(isLightTheme ? R.style.Material_App_Dialog_DatePicker_Light : R.style.Material_App_Dialog_DatePicker)
                        // call sub builder method first
                        .dateRange(1, 1, 1900, 1, 1, 2100)
                        .positiveAction("OK", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                String date = ((DatePickerDialog) dialog).getFormattedDate(SimpleDateFormat.getDateInstance());
                                Toast.makeText(mActivity, "Date is " + date, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .negativeAction("Cancel", new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                Toast.makeText(mActivity, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                fragment = DialogFragment.newInstance(builder);
                fragment.show(getFragmentManager(), null);
                break;
        }

    }
}
