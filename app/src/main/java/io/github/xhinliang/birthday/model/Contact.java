package io.github.xhinliang.birthday.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by xhinliang on 16-1-28.
 * xhinliang@gmail.com
 */
public class Contact extends RealmObject {
    public static final String FIELD_GROUP = "group";

    private String name;
    private String telephone;
    private Date birthday;
    private String description;
    private String group;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


}
