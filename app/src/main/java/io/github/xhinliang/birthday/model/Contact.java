package io.github.xhinliang.birthday.model;


import android.util.Log;

import org.parceler.Parcel;

import java.util.Date;

import io.realm.ContactRealmProxy;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by xhinliang on 16-1-28.
 * xhinliang@gmail.com
 */
@Parcel(implementations = {ContactRealmProxy.class}, value = Parcel.Serialization.BEAN, analyze = {Contact.class})
public class Contact extends RealmObject {
    public static final String FIELD_GROUP = "group";

    @PrimaryKey
    private long createTimeMillis;

    @Required
    private String name;


    private Boolean isLunar;
    private String telephone;
    private Date birthday;
    private String description;
    private String group;
    private String picture;

    public Boolean getIsLunar() {
        return isLunar;
    }

    public void setIsLunar(Boolean isLunar) {
        this.isLunar = isLunar;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }

    public Contact() {
        this.createTimeMillis = System.currentTimeMillis();
    }

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
