package io.github.xhinliang.birthday.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by xhinliang on 16-1-28.
 * xhinliang@gmail.com
 */
public class Group extends RealmObject {
    private String name;
    private RealmList<Contact> contacts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(RealmList<Contact> contacts) {
        this.contacts = contacts;
    }
}
