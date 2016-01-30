package io.github.xhinliang.birthday.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.databinding.ActivityContactDetailsBinding;
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

    private ActivityContactDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_details);
        setSupportActionBar(binding.toolbar);
        setHasBackButton();
        initEvent();
    }

    private void initEvent() {
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
                        dpd.show(getFragmentManager(), "DatePickerDialog");
                    }
                });

        setRxClick(binding.fabAddPhoto)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Intent.ACTION_PICK);//ACTION_OPEN_DOCUMENT
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_PIC);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case SELECT_PIC:
                try {
                    Bitmap bitmap = getCompressBitmap(intent.getData(), MAX_IMAGE_SIZE);
                    binding.setImage(intent.getData());
                    binding.ivPicture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    showSimpleDialog(R.string.fail_to_load_image);
                }
        }
    }

    public Bitmap getCompressBitmap(Uri uri, int maxSize) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        //注意此处的InputStream不需要手动close！！！
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //第二个Rect的参数用来测量图片的边距，省略
        BitmapFactory.decodeStream(inputStream, null, options);
        options.inJustDecodeBounds = false;
        int scale = 1;
        //缩放比,用高或者宽其中较大的一个数据进行计算
        if (options.outWidth > options.outHeight && options.outWidth > maxSize)
            scale = options.outWidth / maxSize;
        if (options.outWidth < options.outHeight && options.outWidth > maxSize)
            scale = options.outHeight / maxSize;
        scale++;
        options.inSampleSize = scale;//设置采样率
        //注意这里的inputStream需要重新生成
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
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
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

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

    private interface setTextCallback {
        void onConfirm(String text);
    }
}
