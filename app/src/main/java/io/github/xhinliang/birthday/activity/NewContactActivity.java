package io.github.xhinliang.birthday.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import io.github.xhinliang.birthday.R;
import io.github.xhinliang.birthday.databinding.ActivityAddContactBinding;
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
public class NewContactActivity extends BaseActivity {
    private ActivityAddContactBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact);
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
                        new MaterialDialog.Builder(NewContactActivity.this)
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

    }

    private void createNewGroup() {
        new MaterialDialog.Builder(this)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .input(R.string.group_name, R.string.nothing, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
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
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();

    }

    private void selectGroup(CharSequence[] groups) {
        new MaterialDialog.Builder(NewContactActivity.this)
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
