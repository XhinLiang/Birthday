package io.github.xhinliang.birthday.model;

import org.parceler.Parcel;

import io.realm.GroupRealmProxy;
import io.realm.RealmObject;

/**
 * Created by xhinliang on 16-1-28.
 * xhinliang@gmail.com
 */
@Parcel(implementations = {GroupRealmProxy.class}, value = Parcel.Serialization.BEAN, analyze = {Group.class})
public class Group extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
