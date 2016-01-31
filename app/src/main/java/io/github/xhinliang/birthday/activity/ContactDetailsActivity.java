package io.github.xhinliang.birthday.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.databinding.ActivityContactDetailsBinding;
import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.birthday.model.Group;
import io.github.xhinliang.lib.activity.BaseActivity;
import io.realm.Realm;
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
public class ContactDetailsActivity extends BaseActivity {

    private static final int SELECT_PIC = 100;
    private static final int MAX_IMAGE_SIZE = 1000;
    private static final String DIALOG_TAG = "DatePickerDialog";

    private ActivityContactDetailsBinding binding;
    private String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_details);
        setSupportActionBar(binding.toolbar);
        setHasBackButton();
        initEvent();
    }

    private void initEvent() {
        //用户未设置图片时没有动画
        binding.ivPicture.setAnimate(false);
        initTextEvent(binding.mrlName, getString(R.string.name), binding.getName(), new setTextCallback() {
            @Override
            public void onConfirm(String text) {
                binding.setName(text);
            }
        });

        initTextEvent(binding.mrlDescription, getString(R.string.description), binding.getDescription(), new setTextCallback() {
            @Override
            public void onConfirm(String text) {
                binding.setDescription(text);
            }
        });

        initTextEvent(binding.mrlTelephone, getString(R.string.telephone), binding.getTelephone(), new setTextCallback() {
            @Override
            public void onConfirm(String text) {
                binding.setTelephone(text);
            }
        });

        setRxClick(binding.mrlGroup)
                .observeOn(Schedulers.io())
                .map(new Func1<Void, CharSequence[]>() {
                    @Override
                    public CharSequence[] call(Void aVoid) {
                        RealmQuery<Group> query = Realm.getDefaultInstance().where(Group.class);
                        RealmResults<Group> groups = query.findAll();
                        if (groups.size() == 0)
                            return null;
                        CharSequence[] groupNames = new CharSequence[groups.size()];
                        for (int i = 0; i < groups.size(); ++i)
                            groupNames[i] = groups.get(i).getName();
                        return groupNames;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<CharSequence[], Boolean>() {
                    @Override
                    public Boolean call(CharSequence[] groups) {
                        if (groups != null)
                            return true;
                        askForCreateNewGroup();
                        return false;
                    }
                })
                .compose(this.<CharSequence[]>bindToLifecycle())
                .subscribe(new Action1<CharSequence[]>() {
                    @Override
                    public void call(CharSequence[] groups) {
                        selectGroup(groups);
                    }
                });

        setRxClick(binding.mrlBirthday)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Calendar defaultDate = new GregorianCalendar(1995, 0, 6);
                        if (binding.getBirthday() != null)
                            defaultDate.setTime(binding.getBirthday());
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, day);
                                binding.setBirthday(calendar.getTime());
                            }
                        };
                        DatePickerDialog dpd = DatePickerDialog.newInstance(listener,
                                defaultDate.get(Calendar.YEAR),
                                defaultDate.get(Calendar.MONTH),
                                defaultDate.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.setThemeDark(true);
                        dpd.vibrate(false);
                        dpd.showYearPickerFirst(true);
                        dpd.show(getFragmentManager(), DIALOG_TAG);
                    }
                });

        setRxClick(binding.fabDone)
                .filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        if (TextUtils.isEmpty(binding.getName())) {
                            showSimpleDialog(R.string.fail_to_add_contact, R.string.name_is_empty);
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<Void, Void>() {
                    @Override
                    public Void call(Void aVoid) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        Contact contact = realm.createObject(Contact.class);
                        contact.setName(binding.getName());
                        contact.setGroup(binding.getGroup());
                        contact.setBirthday(binding.getBirthday());
                        contact.setDescription(binding.getDescription());
                        contact.setTelephone(binding.getTelephone());
                        contact.setPicture(picture);
                        realm.commitTransaction();
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showSimpleDialog(R.string.result, R.string.success);
                    }
                });

        setRxClick(binding.ivPicture)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_PIC);
                    }
                });
    }

    private void askForCreateNewGroup() {
        new MaterialDialog.Builder(ContactDetailsActivity.this)
                .content(R.string.no_group_yet)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        createNewGroup();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case SELECT_PIC:
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
                            return getCompressBitmap(uri);
                        } catch (FileNotFoundException e) {
                            showSimpleDialog(R.string.fail_to_load_image);
                        }
                        return null;
                    }
                })
                .filter(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        return bitmap == null;
                    }
                })
                .filter(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        String savePicture = String.format("avatar_%s_%d", binding.getName(), System.currentTimeMillis());
                        File file = new File(getFilesDir().getAbsolutePath(), savePicture);
                        try {
                            FileOutputStream stream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            stream.close();
                        } catch (java.io.IOException e) {
                            return false;
                        } finally {
                            bitmap.recycle();
                        }
                        picture = savePicture;
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Bitmap>bindToLifecycle())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        binding.ivPicture.setImageBitmap(bitmap);
                        //设置了图片，动画开启
                        binding.ivPicture.setAnimate(true);
                    }
                });
    }


    private void createNewGroup() {
        new MaterialDialog.Builder(this)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .input(R.string.group_name, R.string.nothing, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        addGroupToRealm(input);
                    }
                })
                .show();
    }

    private void addGroupToRealm(CharSequence input) {
        Observable.just(input.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<String, Void>() {
                    @Override
                    public Void call(String s) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        Group group = realm.createObject(Group.class);
                        group.setName(s);
                        realm.commitTransaction();
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showSimpleDialog(R.string.success);
                    }
                });
    }

    private void selectGroup(CharSequence[] groups) {
        new MaterialDialog.Builder(ContactDetailsActivity.this)
                .title(R.string.select_group)
                .items(groups)
                .positiveText(R.string.select)
                .negativeText(R.string.cancel)
                .neutralText(R.string.create)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        binding.setGroup(text.toString());
                        return false;
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        createNewGroup();
                    }
                })
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
        new MaterialDialog.Builder(this)
                .title(title)
                .input(title, origin, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        listener.onConfirm(input.toString());
                    }
                })
                .show();
    }

    public Bitmap getCompressBitmap(Uri uri) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        //注意此处的InputStream不需要手动close！！！
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //第二个Rect的参数用来测量图片的边距，省略
        BitmapFactory.decodeStream(inputStream, null, options);
        options.inJustDecodeBounds = false;
        int scale = 1;
        //缩放比,用高或者宽其中较大的一个数据进行计算
        if (options.outWidth > options.outHeight && options.outWidth > MAX_IMAGE_SIZE)
            scale = options.outWidth / MAX_IMAGE_SIZE;
        if (options.outWidth < options.outHeight && options.outWidth > MAX_IMAGE_SIZE)
            scale = options.outHeight / MAX_IMAGE_SIZE;
        scale++;
        options.inSampleSize = scale;//设置采样率
        //注意这里的inputStream需要重新生成
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
    }

    private interface setTextCallback {
        void onConfirm(String text);
    }
}
