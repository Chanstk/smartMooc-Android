package com.example.lenovo.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * Created by lenovo on 2015/11/3.
 */
public class videoBean implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    public String title;
    public String provider;
    public Drawable photo;
    public String url;
    public int money;
    public int numbers;
    public String detail_course;
    public String teacher;
    public String detail_teacher;
    public String category;
    public videoBean(String title, Drawable photo, String url,  String provider,int money, int numbers, String detail_course, String teacher, String detail_teacher,String category) {
        this.title = title;
        this.provider = provider;
        this.photo = photo;
        this.url = url;
        this.money = money;
        this.numbers = numbers;
        this.detail_course = detail_course;
        this.teacher = teacher;
        this.detail_teacher = detail_teacher;
        this.category = category;
    }
    public videoBean(){

    }
}
