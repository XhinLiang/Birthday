package io.github.xhinliang.birthday.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import com.rey.material.dialog.DatePickerDialog;
import com.rey.material.dialog.Dialog;
import com.rey.material.dialog.SimpleDialog;
import com.rey.material.widget.EditText;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.databinding.ActivityContactDetailsBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.model.Group;
import io.github.xhinliang.birthday.rx.RxCheckBox;
import io.github.xhinliang.birthday.util.XLog;
import io.github.xhinliang.lib.activity.RealmActivity;
import io.github.xhinliang.lib.util.ImageUtils;
import io.github.xhinliang.lunarcalendar.LunarCalendar;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xhinliang on 16-1-28.
 * xhinliang@gmail.com
 */
public class ContactDetailsActivity extends RealmActivity {

    private static final int REQUEST_SELECT_PIC = 0x100;
    private static final String DIALOG_TAG = "DatePickerDialog";
    private static final String TAG = "ContactDetailsActivity";
    public static final String EXTRA_CONTACT = "Contact";


    private ActivityContactDetailsBinding binding;
    private String pictureName;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_details);
        setSupportActionBar(binding.toolbar);
        setHasBackButton();
        initEvent();
        checkIntent();
    }

    private void checkIntent() {
        Parcelable parcelable = getIntent().getParcelableExtra(EXTRA_CONTACT);
        if (parcelable == null)
            return;
        contact = Parcels.unwrap(parcelable);
        binding.setName(contact.getName());
        binding.setGroup(contact.getGroup());
        binding.setTelephone(contact.getTelephone());
        binding.setBirthday(contact.getBirthday());
        binding.setDescription(contact.getDescription());
    }

    private void initEvent() {
        // 用户未设置图片时没有动画
        binding.ivPicture.setAnimate(false);

        initTextEvent(binding.ivName, getString(R.string.name), binding.getName(), new setTextCallback() {
            @Override
            public void onConfirm(String text) {
                binding.setName(text);
            }
        });

        initTextEvent(binding.ivDescription, getString(R.string.description), binding.getDescription(), new setTextCallback() {
            @Override
            public void onConfirm(String text) {
                binding.setDescription(text);
            }
        });

        initTextEvent(binding.ivTelephone, getString(R.string.telephone), binding.getTelephone(), new setTextCallback() {
            @Override
            public void onConfirm(String text) {
                binding.setTelephone(text);
            }
        });

        setRxClick(binding.ivGroup)
                .flatMap(new Func1<Void, Observable<RealmResults<Group>>>() {
                    @Override
                    public Observable<RealmResults<Group>> call(Void aVoid) {
                        RealmQuery<Group> query = realm.where(Group.class);
                        return query.findAllAsync().asObservable();
                    }
                })
                .filter(new Func1<RealmResults<Group>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Group> groups) {
                        return groups.isLoaded();
                    }
                })
                .map(new Func1<RealmResults<Group>, CharSequence[]>() {
                    @Override
                    public CharSequence[] call(RealmResults<Group> groups) {
                        if (groups.size() == 0)
                            return null;
                        CharSequence[] groupNames = new CharSequence[groups.size()];
                        for (int i = 0; i < groups.size(); ++i)
                            groupNames[i] = groups.get(i).getName();
                        return groupNames;
                    }
                })
                .compose(this.<CharSequence[]>bindToLifecycle())
                .subscribe(new Action1<CharSequence[]>() {
                    @Override
                    public void call(CharSequence[] groups) {
                        if (groups == null)
                            askForCreateNewGroup();
                        else
                            selectGroup(groups);
                    }
                });

        Func1<Void, Boolean> filter = new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                if (TextUtils.isEmpty(binding.getName())) {
                    showSimpleDialog(R.string.fail_to_add_contact, R.string.name_is_empty);
                    return false;
                }
                return true;
            }
        };

        setRxClick(binding.ivBirthday)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Date initDate = binding.getBirthday();
                        if (initDate == null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_MONTH, 6);
                            calendar.set(Calendar.MONTH, Calendar.JANUARY);
                            calendar.set(Calendar.YEAR, 1995);
                            initDate = calendar.getTime();
                        }
                        new DatePickerDialog.Builder()
                                .dateRange(1, 1, 1900, 1, 1, 2100)
                                .initDate(initDate)
                                .positiveAction(getString(R.string.confirm), new Dialog.Action1() {
                                    @Override
                                    public void onAction(Dialog dialog) {
                                        LunarCalendar date = ((DatePickerDialog) dialog).getLunarCalendar();
                                        binding.setBirthday(date.getDate());
                                    }
                                })
                                .negativeAction(getString(R.string.cancel), null)
                                .build(ContactDetailsActivity.this)
                                .show();
                    }
                });


        setRxClick(binding.fabDone)
                .filter(filter)
                .map(new Func1<Void, Void>() {
                    @Override
                    public Void call(Void aVoid) {
                        realm.beginTransaction();
                        // 不等于空时为修改联系人的情况
                        if (contact == null)
                            contact = realm.createObject(Contact.class);
                        contact.setName(binding.getName());
                        contact.setGroup(binding.getGroup());
                        contact.setBirthday(binding.getBirthday());
                        contact.setDescription(binding.getDescription());
                        contact.setTelephone(binding.getTelephone());
                        contact.setPicture(pictureName);
                        contact.setIsLunar(binding.getIsLunar());
                        realm.commitTransaction();
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showSimpleDialog(R.string.result, R.string.success, new Dialog.Action1() {
                            @Override
                            public void onAction(Dialog dialog) {
                                finish();
                            }
                        });
                    }
                });

        setRxClick(binding.ivPicture)
                .filter(filter)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_SELECT_PIC);
                    }
                });

        RxCheckBox.checkedChange(binding.cbLunar)
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isChecked) {
                        XLog.d(TAG, "CheckedChange");
                        binding.setIsLunar(isChecked);
                        XLog.d(TAG, "isLunar " + binding.getIsLunar());
                    }
                });
    }

    private void askForCreateNewGroup() {
        new SimpleDialog.Builder()
                .message(getString(R.string.no_group_yet))
                .positiveAction(getString(R.string.confirm), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        createNewGroup();
                    }
                })
                .negativeAction(getString(R.string.cancel), null)
                .build(this)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_SELECT_PIC:
                handlePicture(intent);
        }
    }

    private void handlePicture(Intent intent) {
        Observable.just(intent.getData())
                .observeOn(Schedulers.io())
                .map(new Func1<Uri, Bitmap>() {
                    @Override
                    public Bitmap call(Uri uri) {
                        try {
                            return ImageUtils.getCompressBitmap(uri, getContentResolver());
                        } catch (FileNotFoundException e) {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        if (bitmap == null) {
                            showSimpleDialog(R.string.fail_to_load_image);
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .filter(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        String savePath = String.format("avatar_%s", binding.getName());
                        File file = new File(getFilesDir().getAbsolutePath(), savePath);
                        try {
                            FileOutputStream stream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            stream.close();
                        } catch (java.io.IOException e) {
                            return false;
                        }
                        pictureName = savePath;
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Bitmap>bindToLifecycle())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        binding.ivPicture.setImageBitmap(bitmap);
                        // 设置了图片，动画开启
                        binding.ivPicture.setAnimate(true);
                    }
                });
    }


    private void createNewGroup() {
        new SimpleDialog.Builder()
                .title(getString(R.string.create_group))
                .contentView(R.layout.dialog_input)
                .positiveAction(getString(R.string.confirm), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        String input = ((EditText) dialog.findViewById(R.id.custom_et)).getText().toString();
                        realm.beginTransaction();
                        Group group = realm.createObject(Group.class);
                        group.setName(input);
                        realm.commitTransaction();
                    }
                })
                .negativeAction(getString(R.string.cancel), null)
                .build(this)
                .show();
    }

    private void selectGroup(CharSequence[] groups) {
        new SimpleDialog.Builder()
                .items(groups, 0)
                .title(getString(R.string.select_group))
                .positiveAction(getString(R.string.select), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        CharSequence groupName = ((SimpleDialog) dialog).getSelectedValue();
                        binding.setGroup(groupName.toString());
                    }
                })
                .neutralAction(getString(R.string.create), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        createNewGroup();
                    }
                })
                .negativeAction(getString(R.string.cancel), null)
                .build(this)
                .show();
    }

    private void initTextEvent(View view, final String title, final CharSequence origin, final setTextCallback listener) {
        setRxClick(view)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setTextArg(title, origin, listener);
                    }
                });
    }

    private void setTextArg(CharSequence title, CharSequence origin, final setTextCallback listener) {
        Dialog dialog = new SimpleDialog.Builder()
                .title(title)
                .contentView(R.layout.dialog_input)
                .positiveAction(getString(R.string.confirm), new Dialog.Action1() {
                    @Override
                    public void onAction(Dialog dialog) {
                        String input = ((EditText) dialog.findViewById(R.id.custom_et)).getText().toString();
                        listener.onConfirm(input);
                    }
                })
                .negativeAction(getString(R.string.cancel), null)
                .build(this);
        EditText editText = ((EditText) dialog.findViewById(R.id.custom_et));
        editText.setText(origin);
        editText.setHint(title);
        dialog.show();
    }

    private interface setTextCallback {
        void onConfirm(String text);
    }
}
