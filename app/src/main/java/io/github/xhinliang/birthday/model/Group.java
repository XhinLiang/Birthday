package io.github.xhinliang.birthday.model;

import io.realm.RealmObject;

/**
 * Created by xhinliang on 16-1-28.
 * xhinliang@gmail.com
 */
public class Group extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
